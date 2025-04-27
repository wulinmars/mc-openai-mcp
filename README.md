# Minecraft AI Chat Server

一个基于 Node.js 的 Minecraft 服务器，集成了 AI 聊天功能，让玩家可以在游戏中与 AI 进行对话。

## 功能特点

- 支持可配置的 Minecraft 版本
- 集成 OpenAI GPT-3.5 聊天功能
- 支持多玩家同时对话
- 交互式配置向导
- 支持命令行和环境变量两种方式配置 API key
- 自动创建和更新配置文件

## 系统要求

- [Node.js](https://nodejs.org/) 14.0.0 或更高版本
  - [Windows 安装指南](https://nodejs.org/en/download/package-manager/#windows)
  - [macOS 安装指南](https://nodejs.org/en/download/package-manager/#macos)
  - [Linux 安装指南](https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions)
- [pnpm](https://pnpm.io/) 包管理器
  - 安装命令：`npm install -g pnpm`
  - [详细安装指南](https://pnpm.io/installation)
- Minecraft 客户端（版本需与服务器配置匹配）
- OpenAI API key

## 安装步骤

1. 安装必要的工具：

   - 安装 Node.js（见上方链接）
   - 安装 pnpm：`npm install -g pnpm`

2. 克隆仓库：

```bash
git clone [repository-url]
cd mcp-study
```

3. 安装依赖：

```bash
pnpm install
```

4. 配置环境：

   - 复制 `.env.example` 为 `.env`
   - 在 `.env` 文件中设置你的 OpenAI API key：
     ```
     OPENAI_API_KEY=your_api_key_here
     ```
   - 或者通过命令行参数传入 API key（见启动说明）

## 启动服务器

### 使用环境变量中的 API key

```bash
pnpm start
```

### 使用命令行参数传入 API key

```bash
pnpm run start:with-key YOUR_API_KEY
```

启动后，你会看到交互式配置向导，可以配置以下选项：

- 是否启用在线模式（默认：false）
- 服务器版本（默认：1.16.5）
- 最大玩家数量（默认：20）
- 是否启用创造模式（默认：false）

## 游戏内使用

1. 使用 Minecraft 客户端连接到服务器：

   - 服务器地址：localhost
   - 端口：25565（或配置的端口）
   - 注意：客户端版本必须与服务器配置的版本一致

2. 与 AI 对话：
   - 在聊天框中输入 `!ai 你的问题`
   - 例如：`!ai 告诉我关于 Minecraft 的一些有趣事实`
   - AI 会以配置的颜色回复你的消息

## 配置文件说明

服务器使用两个主要的配置文件：`.env` 和 `config.json`。

### 配置文件模板

项目提供了两个配置文件模板：

- `.env.example`：环境变量配置模板
- `config.example.json`：服务器配置模板

你可以基于这些模板创建自己的配置文件：

```bash
# 复制环境变量配置模板
cp .env.example .env

# 复制服务器配置模板（可选，服务器会自动创建）
cp config.example.json config.json
```

### 配置项说明

`config.json` 包含以下配置项：

#### 服务器配置 (server)

```json
{
  "server": {
    "port": 25565, // Minecraft 服务器端口
    "version": "1.16.5", // Minecraft 版本号
    "motd": "AI Chat MCP Server", // 服务器描述
    "onlineMode": false, // 是否启用正版验证
    "maxPlayers": 20, // 最大玩家数量
    "viewDistance": 10, // 视距（区块）
    "difficulty": 1, // 游戏难度（0:和平 1:简单 2:普通 3:困难）
    "gamemode": 0, // 游戏模式（0:生存 1:创造 2:冒险 3:旁观）
    "spawnProtection": 16 // 出生点保护范围
  }
}
```

#### AI 配置 (ai)

```json
{
  "ai": {
    "model": "gpt-3.5-turbo-0125", // OpenAI 最新免费模型（2024年1月发布）
    "systemPrompt": "You are a helpful assistant in a Minecraft server.", // AI 系统提示词
    "maxHistory": 10, // 每个玩家的对话历史记录最大长度
    "responseColor": "green" // AI 回复消息的颜色
  }
}
```

### 配置文件管理

1. 自动创建：

   - 首次启动时，如果 `config.json` 不存在，服务器会自动创建并使用默认配置
   - 你可以通过交互式配置向导修改配置

2. 手动修改：

   - 你可以直接编辑 `config.json` 文件
   - 也可以复制 `config.example.json` 作为起点
   - 修改后重启服务器生效

3. 配置优先级：
   - API Key：命令行参数 > 环境变量 > 交互式输入
   - 服务器配置：交互式配置 > 配置文件 > 默认值

### 配置注意事项

1. 版本兼容：

   - 确保 `version` 与你的客户端版本匹配
   - 支持的版本取决于 `minecraft-protocol` 库的兼容性

2. 游戏模式：

   - `gamemode` 设置为 1 时为创造模式
   - 设置为 0 时为生存模式

3. AI 配置：
   - `maxHistory` 影响内存使用，建议不要设置太大
   - `responseColor` 支持的颜色：black, dark_blue, dark_green, dark_aqua, dark_red, dark_purple, gold, gray, dark_gray, blue, green, aqua, red, light_purple, yellow, white

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

服务器支持多个 Minecraft 版本，具体支持的版本取决于 `minecraft-protocol` 库的兼容性。你可以在配置向导中设置所需的版本。

## 贡献

欢迎提交 Issue 和 Pull Request 来帮助改进这个项目。

## 许可证

ISC
