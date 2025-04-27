const mc = require("minecraft-protocol");
const { Configuration, OpenAIApi } = require("openai");
const fs = require("fs");
require("dotenv").config();

// 加载配置文件
const config = JSON.parse(fs.readFileSync("./config.json", "utf8"));

// 从命令行参数或环境变量获取 API key
function getApiKey() {
  // 检查命令行参数
  const args = process.argv.slice(2);
  for (let i = 0; i < args.length; i++) {
    if (args[i] === "--api-key" && args[i + 1]) {
      return args[i + 1];
    }
  }

  // 如果命令行没有提供，则使用环境变量
  return process.env.OPENAI_API_KEY;
}

const apiKey = getApiKey();
if (!apiKey) {
  console.error(
    "Error: No API key provided. Please provide an API key either through:"
  );
  console.error("1. Command line: node server.js --api-key YOUR_API_KEY");
  console.error("2. Environment variable: OPENAI_API_KEY in .env file");
  process.exit(1);
}

// 初始化 OpenAI
const configuration = new Configuration({
  apiKey: apiKey,
});
const openai = new OpenAIApi(configuration);

const server = mc.createServer({
  "online-mode": config.server.onlineMode,
  port: config.server.port,
  version: config.server.version,
  motd: config.server.motd,
  "max-players": config.server.maxPlayers,
  "view-distance": config.server.viewDistance,
  difficulty: config.server.difficulty,
  gamemode: config.server.gamemode,
  "spawn-protection": config.server.spawnProtection,
});

// 存储每个玩家的对话历史
const chatHistories = new Map();

async function getAIResponse(username, message) {
  try {
    const history = chatHistories.get(username) || [];
    history.push({ role: "user", content: message });

    // 限制历史记录长度
    if (history.length > config.ai.maxHistory * 2) {
      history.splice(0, history.length - config.ai.maxHistory * 2);
    }

    const completion = await openai.createChatCompletion({
      model: config.ai.model,
      messages: [
        { role: "system", content: config.ai.systemPrompt },
        ...history,
      ],
    });

    const response = completion.data.choices[0].message.content;
    history.push({ role: "assistant", content: response });
    chatHistories.set(username, history);

    return response;
  } catch (error) {
    console.error("AI Error:", error);
    return "Sorry, I encountered an error while processing your message.";
  }
}

server.on("login", function (client) {
  console.log("New player connected:", client.username);

  // 发送欢迎消息
  client.write("chat", {
    message: JSON.stringify({
      text: `Welcome ${client.username}! You can chat with me by typing messages in the chat.`,
      color: "yellow",
    }),
  });

  // 监听玩家消息
  client.on("chat", async function (packet) {
    const message = packet.message;
    if (message.startsWith("!ai ")) {
      const aiMessage = message.substring(4);
      const response = await getAIResponse(client.username, aiMessage);

      // 发送 AI 回复给玩家
      client.write("chat", {
        message: JSON.stringify({
          text: `AI: ${response}`,
          color: config.ai.responseColor,
        }),
      });
    }
  });
});

console.log(`AI Chat MCP Server started on port ${config.server.port}`);
