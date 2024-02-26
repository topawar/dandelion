# ${name}

> ${description}
> 作者：${author}

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
generator generate <#list modelConfig.models as modelInfo>-${modelInfo.abbr} </#list>
```

## 参数说明
| 名称                   | 类型              | 描述                     | 默认值                      | 缩写               |
| ---------------------- | ----------------- | ------------------------ | --------------------------- | ------------------ |
<#list modelConfig.models as modelInfo>
| ${modelInfo.fieldName} | ${modelInfo.type} | ${modelInfo.description} | ${modelInfo.defaultValue?c} | -${modelInfo.abbr} |
</#list>
