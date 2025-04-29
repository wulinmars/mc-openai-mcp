# MCP Plugin

一个基于 Paper 的 Minecraft 插件，集成了 OpenAI GPT-3.5 聊天功能，让玩家可以在游戏中与 AI 进行对话。

## 功能特点

- 支持 Paper/Spigot 1.20.4 或更高版本
- 集成 OpenAI GPT-3.5 Turbo 聊天功能
- 异步处理避免服务器卡顿
- 支持彩色聊天消息
- 可配置的 OpenAI API 设置

## 系统要求

- Paper/Spigot 1.20.4 或更高版本
- Java 17 或更高版本
- OpenAI API key

## 安装步骤

1. 下载最新版本的插件 JAR 文件
2. 将 JAR 文件放入服务器的 `plugins` 文件夹
3. 重启服务器
4. 编辑 `plugins/MCPPlugin/config.yml` 配置文件
5. 在配置文件中设置你的 OpenAI API key

## 配置文件

首次运行时会自动创建默认配置文件 `config.yml`：

```yaml
openai:
  api-key: "your-api-key-here"
```

## 游戏内使用

在游戏中，使用 `/mcp` 命令后跟你的问题：

```
/mcp 告诉我关于 Minecraft 的一些有趣事实
```

## 从源码构建

1. 克隆仓库：

```bash
git clone git@github.com:wulinmars/mc-openai-mcp.git
cd mcp-study
```

2. 使用 Maven 构建：

```bash
mvn clean package
```

3. 构建后的 JAR 文件将位于 `target` 目录

## 注意事项

1. 确保服务器运行在 Paper/Spigot 1.20.4 或更高版本
2. 需要有效的 OpenAI API key
3. 建议在配置文件中设置 API key，而不是硬编码
4. 异步处理确保服务器性能不受影响

## 故障排除

1. 如果插件无法加载：

   - 检查服务器版本是否兼容
   - 确认 Java 版本是否为 17 或更高
   - 查看服务器控制台是否有错误信息

2. 如果 AI 不响应：
   - 检查 API key 是否正确配置
   - 确认网络连接是否正常
   - 查看服务器控制台是否有错误信息

## 贡献

欢迎提交 Issue 和 Pull Request 来帮助改进这个项目。

## 许可证

MIT
