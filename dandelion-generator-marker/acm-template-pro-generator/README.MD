# acm-template-pro-generator

> ACM 示例模板生成器
> 作者：topawar

## 项目介绍
>代码生成器，通过命令行快速生成你需要的代码;

## 使用说明
执行根目录下的脚本文件
- windows : exec.bat
- linux : exec
``` shell
generate <子命令>
```

## 示例
```
generator --help
```

## 参数说明
| 名称                   | 类型              | 描述                     | 默认值                      | 缩写               |
| ---------------------- | ----------------- | ------------------------ | --------------------------- | ------------------ |
|    loop | boolean | 是否生成循环 | false | -l |
|    needGit | boolean | 是否生成git文件 | true | -n |
|    mainTemplate | MainTemplate | 核心模板 | loop |  |
