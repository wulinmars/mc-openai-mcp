# Minecraft AI Chat Server

一个基于 Node.js 的 Minecraft 服务器，集成了 AI 聊天功能，让玩家可以在游戏中与 AI 进行对话。

## 功能特点

- 支持可配置的 Minecraft 版本
- 集成 OpenAI GPT-3.5 聊天功能
- 支持多玩家同时对话
- 可配置的服务器参数
- 支持命令行和环境变量两种方式配置 API key

## 系统要求

- Node.js 14.0.0 或更高版本
- Minecraft 客户端（版本需与服务器配置匹配）
- OpenAI API key

## 安装步骤

1. 克隆仓库：

```bash
git clone [repository-url]
cd mcp-study
```

2. 安装依赖：

```bash
npm install
```

3. 配置环境：

   - 复制 `.env.example` 为 `.env`
   - 在 `.env` 文件中设置你的 OpenAI API key：
     ```
     OPENAI_API_KEY=your_api_key_here
     ```
   - 或者通过命令行参数传入 API key（见启动说明）

4. 配置服务器：
   - 编辑 `config.json` 文件调整服务器设置
   - 可配置项包括：
     - 服务器端口
     - 游戏版本（支持多个 Minecraft 版本）
     - MOTD（服务器描述）
     - 最大玩家数
     - 视距
     - 难度
     - 游戏模式
     - 出生点保护范围
     - AI 模型设置
     - 对话历史长度
     - 回复消息颜色

## 启动服务器

### 使用环境变量中的 API key

```bash
npm start
```

### 使用命令行参数传入 API key

```bash
npm run start:with-key YOUR_API_KEY
```

## 游戏内使用

1. 使用 Minecraft 客户端连接到服务器：

   - 服务器地址：localhost
   - 端口：25565（或 config.json 中配置的端口）
   - 注意：客户端版本必须与服务器配置的版本一致

2. 与 AI 对话：
   - 在聊天框中输入 `!ai 你的问题`
   - 例如：`!ai 告诉我关于 Minecraft 的一些有趣事实`
   - AI 会以配置的颜色回复你的消息

## 配置文件说明

`config.json` 文件包含以下主要配置：

### 服务器配置

```json
{
  "server": {
    "port": 25565,
    "version": "1.16.5", // 可配置为其他支持的 Minecraft 版本
    "motd": "AI Chat MCP Server",
    "onlineMode": false,
    "maxPlayers": 20,
    "viewDistance": 10,
    "difficulty": 1,
    "gamemode": 0,
    "spawnProtection": 16
  }
}
```

### AI 配置

```json
{
  "ai": {
    "model": "gpt-3.5-turbo",
    "systemPrompt": "You are a helpful assistant in a Minecraft server.",
    "maxHistory": 10,
    "responseColor": "green"
  }
}
```

## 注意事项

1. 确保 Minecraft 客户端版本与服务器配置的版本一致
2. API key 可以通过环境变量或命令行参数提供
3. 服务器运行在离线模式，不需要验证 Minecraft 账号
4. 每个玩家的对话历史是独立的
5. 对话历史长度受 `maxHistory` 配置限制

## 故障排除

1. 如果无法连接服务器：

   - 检查端口是否被占用
   - 确认 Minecraft 版本是否与服务器配置匹配
   - 验证服务器是否正在运行

2. 如果 AI 不响应：
   - 检查 API key 是否正确配置
   - 确认网络连接是否正常
   - 查看服务器控制台是否有错误信息

## 支持的 Minecraft 版本

服务器支持多个 Minecraft 版本，具体支持的版本取决于 `minecraft-protocol` 库的兼容性。你可以在 `config.json` 中的 `server.version` 字段配置所需的版本。

## 贡献

欢迎提交 Issue 和 Pull Request 来帮助改进这个项目。

## 许可证

ISC
