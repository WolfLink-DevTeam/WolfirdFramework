# Wolfird - 简洁规范的玩法开发框架
![开源协议](https://img.shields.io/github/license/WolfLink-DevTeam/WolfirdFramework?style=for-the-badge)
![Stars](https://img.shields.io/github/stars/WolfLink-DevTeam/WolfirdFramework?style=for-the-badge)
![Last Commit](https://img.shields.io/github/last-commit/WolfLink-DevTeam/WolfirdFramework?style=for-the-badge)
[![Stargazers over time](https://starchart.cc/WolfLink-DevTeam/WolfirdFramework.svg)](https://starchart.cc/WolfLink-DevTeam/WolfirdFramework)
### 简介
这是一个面向敏捷开发的 Minecraft Bukkit 插件玩法开发框架，集成了 MongoDB 和 Google Guice，并对Bukkit提供的一些原生功能进行了二次封装，以便更快地进行子插件的开发。

### 主类
子插件主类不再直接继承 JavaPlugin，而是从 AddonPlugin、ModePlugin、SystemPlugin 中选择一个继承。
AddonPlugin 描述的是一个小功能拓展插件；ModePlugin 描述的是一个游戏模式拓展插件，例如 UHC 模式、猎人游戏模式等；SystemPlugin 描述逇一个大型玩法系统拓展插件。
```java
// 这是一个示例模块插件的主类
public final class WolfirdTestAddon1 extends AddonPlugin {
	// 具体方法...
}
```
### 事件监听器(还不够完善)
不再需要直接实现 Listener 接口，而是继承 WolfirdListener 抽象类。
WolfirdListener 提供了比Bukkit封装的Listener更加方便的注册和取消注册的方式，只需要通过setEnabled改变其状态即可，另外事件监听方法仍然需要使用 @EventHandler 进行标注。
可能存在的问题：
在事件监听器生效期间创建 BukkitTask 延迟任务，在任务未生效前监听器被注销，此时任务仍未被注销。
```java
// 这是一个示例事件监听器
public class TestListener extends WolfirdListener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        System.out.println(e.getPlayer().getName());
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        System.out.println(e.getBlock().getType().name());
    }
}

// 启用监听器
wolfirdListener.setEnabled(true);
// 禁用监听器
wolfirdListener.setEnabled(false);
```
### 指令
只需要实现 WolfirdCommand 抽象类，之后调用插件主类(AddonPlugin、ModePlugin、SystemPlugin)的registerCommand()方法即可注册，不需要再自己实现setExecutor、编写配置文件之类的。
```java
public class CmdHelp extends WolfirdCommand {

	// 省略一些不关键的代码
    
    public CmdHelp() {
        super(false, true, true, "wolfird help","查看Wolfird框架插件帮助");
    }

    @Override
    protected void execute(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage("§8[ "+notifier.getPrefix()+" §8]");
        sender.sendMessage(" ");
        for (WolfirdCommand command : commandContainer.getCommands()) {
            sender.sendMessage("§f"+command.getCommandTemplate()+" §8- §7"+command.getHelpMessage());
        }
        sender.sendMessage(" ");
    }
}
CmdHelp cmdHelpInstance = new CmdHelp();
// 注册指令
MyPlugin.getInstance().registerCommand(cmdHelpInstance);
// 取消注册指令
MyPlugin.getInstance().unregisterCommand(cmdHelpInstance);
```
### 配置文件
主要的配置文件都是存放在 MongoDB 中的，开发者需要创建子插件独立的配置文件时只需要继承 BaseConfig 类并创建单例，之后需要给定默认配置清单，包括配置路径、配置值，然后就可以调用封装好的 load() update() save() 方法便捷完成配置文件的修改和持久化操作。
```java
public class FrameworkConfig extends BaseConfig{
    public FrameworkConfig(){
         super("wolfird_config");
    }
}
```
### 数据库
数据库部分主要指的是POJO映射，框架封装了一个 EntityRepository<E> 类，泛型类型E作为其实体类。该实体类必须有以下特点：

1. 由 @Data 注解标注，或手动实现其所有的get、set、hash等基本数据方法
2. 由 @MongoTable 注解标注，该注解需要指定一个值作为该实体类对应的数据库表单名称
3. 实体类中有且只能有唯一元素被@PrimaryKey标注，该元素将被作为主键并创建索引，值唯一
4. 实体类中只能有基本数据类型，如果涉及到复杂类型会报错

使用时需要自行创建 EntityRepository<E> 对象。
```java
EntityRepository<TestEntity> repo = new EntityRepository<>(TestEntity.class);
```
### 日志
提供了便捷的 SubPluginNotifier 封装，继承自 BaseNotifier，前缀名称通过 plugin.yml 中的 prefix 配置项进行设置，可以带有颜色，如 §9Wolfird。通过子插件主类的 notifier 成员变量获取消息通知器。

