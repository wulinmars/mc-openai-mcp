const mc = require("minecraft-protocol");
const OpenAI = require("openai");
const fs = require("fs");
const readline = require("readline");
require("dotenv").config();

// 创建 readline 接口
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

// 配置默认值
const defaultConfig = {
  server: {
    port: 25565,
    version: "1.16.5",
    motd: "AI Chat MCP Server",
    onlineMode: false,
    maxPlayers: 20,
    viewDistance: 10,
    difficulty: 1,
    gamemode: 0,
    spawnProtection: 16,
  },
  ai: {
    model: "gpt-3.5-turbo",
    systemPrompt: "You are a helpful assistant in a Minecraft server.",
    maxHistory: 10,
    responseColor: "green",
  },
};

// 检查并创建配置文件
function ensureConfigFile() {
  if (!fs.existsSync("./config.json")) {
    console.log("未找到配置文件，正在创建默认配置...");
    fs.writeFileSync("./config.json", JSON.stringify(defaultConfig, null, 2));
    console.log("默认配置文件已创建");
    return defaultConfig;
  }
  return JSON.parse(fs.readFileSync("./config.json", "utf8"));
}

// 从命令行参数或环境变量获取 API key
function getApiKey() {
  const args = process.argv.slice(2);
  for (let i = 0; i < args.length; i++) {
    if (args[i] === "--api-key" && args[i + 1]) {
      return args[i + 1];
    }
  }
  return process.env.OPENAI_API_KEY;
}

// 交互式配置函数
function promptUser(question, defaultValue) {
  return new Promise((resolve) => {
    const prompt = defaultValue
      ? `${question} (默认: ${defaultValue}): `
      : `${question}: `;
    rl.question(prompt, (answer) => {
      resolve(answer || defaultValue);
    });
  });
}

async function configureServer() {
  console.log("欢迎使用 Minecraft AI Chat Server 配置向导");
  console.log("----------------------------------------");

  // 获取 API key
  let apiKey = getApiKey();
  if (!apiKey) {
    apiKey = await promptUser("请输入 OpenAI API key");
  }

  // 加载或创建配置
  const config = ensureConfigFile();

  // 询问用户是否要修改配置
  const modifyConfig = await promptUser("是否要修改服务器配置 (y/n)", "n");

  if (modifyConfig.toLowerCase() === "y") {
    const onlineMode = await promptUser(
      "是否启用在线模式 (true/false)",
      config.server.onlineMode.toString()
    );
    config.server.onlineMode = onlineMode.toLowerCase() === "true";

    const version = await promptUser("请输入服务器版本", config.server.version);
    config.server.version = version;

    const maxPlayers = await promptUser(
      "请输入最大玩家数量",
      config.server.maxPlayers.toString()
    );
    config.server.maxPlayers = parseInt(maxPlayers, 10);

    const creativeMode = await promptUser(
      "是否启用创造模式 (true/false)",
      (config.server.gamemode === 1).toString()
    );
    config.server.gamemode = creativeMode.toLowerCase() === "true" ? 1 : 0;

    // 保存配置
    fs.writeFileSync("./config.json", JSON.stringify(config, null, 2));
    console.log("\n配置已更新到 config.json");
  }

  // 关闭 readline 接口
  rl.close();
  return { apiKey, config };
}

async function startServer() {
  try {
    const { apiKey, config } = await configureServer();

    // 初始化 OpenAI
    const openai = new OpenAI({
      apiKey: apiKey,
    });

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

        const completion = await openai.chat.completions.create({
          model: config.ai.model,
          messages: [
            { role: "system", content: config.ai.systemPrompt },
            ...history,
          ],
        });

        const response = completion.choices[0].message.content;
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

    console.log("\n服务器配置信息:");
    console.log("----------------------------------------");
    console.log("- 版本:", config.server.version);
    console.log("- 在线模式:", config.server.onlineMode ? "是" : "否");
    console.log("- 最大玩家数:", config.server.maxPlayers);
    console.log(
      "- 游戏模式:",
      config.server.gamemode === 1 ? "创造模式" : "生存模式"
    );
    console.log("- 端口:", config.server.port);
    console.log("----------------------------------------");
    console.log("AI Chat MCP Server 已成功启动！");
    console.log("按 Ctrl+C 停止服务器");
  } catch (error) {
    console.error("启动服务器失败:", error);
    process.exit(1);
  }
}

startServer();
