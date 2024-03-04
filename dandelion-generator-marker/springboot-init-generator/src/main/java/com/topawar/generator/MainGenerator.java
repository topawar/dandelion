package com.topawar.generator;

import com.topawar.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;


/**
* @author topawar
*/
public class MainGenerator {

public static void doGenerate(DataModel dataModel) throws TemplateException, IOException {
    String inputRootPath =".source/springboot-init";
    String outputRootPath ="generate";
    String inputPath;
    String outputPath;
            String password=dataModel.mysqlConfig.password;
            String url=dataModel.mysqlConfig.url;
            String username=dataModel.mysqlConfig.username;
            String description=dataModel.docsConfig.description;
            String title=dataModel.docsConfig.title;
            String version=dataModel.docsConfig.version;
        boolean needDocs= dataModel.needDocs;
        boolean needPost= dataModel.needPost;
        boolean needCors= dataModel.needCors;
        boolean needEs= dataModel.needEs;
        String basePackage= dataModel.basePackage;
        boolean needRedis= dataModel.needRedis;

        if(needPost){
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/entity/Post.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/entity/Post.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostAddRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostAddRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/resources/mapper/PostMapper.xml.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/resources/mapper/PostMapper.xml").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/service/impl/PostServiceImpl.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/service/impl/PostServiceImpl.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostUpdateRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostUpdateRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/controller/PostController.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/controller/PostController.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostQueryRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostQueryRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/mapper/PostMapper.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/mapper/PostMapper.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/service/PostService.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/service/PostService.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
       }
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostEsDTO.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/post/PostEsDTO.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/common/ErrorCode.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/common/ErrorCode.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
             outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/exception/BusinessException.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/exception/BusinessException.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/common/PageRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/common/PageRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/config/MyBatisPlusConfig.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/config/MyBatisPlusConfig.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/enums/UserRoleEnum.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/enums/UserRoleEnum.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/exception/GlobalExceptionHandler.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/exception/GlobalExceptionHandler.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
        if(needDocs){
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/config/Knife4jConfig.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/config/Knife4jConfig.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
         }
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/service/impl/UserServiceImpl.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/service/impl/UserServiceImpl.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserRegisterRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserRegisterRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "pom.xml.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "pom.xml").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/resources/mapper/UserMapper.xml.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/resources/mapper/UserMapper.xml").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/common/DeleteRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/common/DeleteRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/constant/UserConstant.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/constant/UserConstant.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserQueryRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserQueryRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/MainApplication.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/MainApplication.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserAddRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserAddRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/entity/User.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/entity/User.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/common/ResultUtils.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/common/ResultUtils.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/mapper/UserMapper.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/mapper/UserMapper.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
        if(needCors){
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/config/CorsConfig.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/config/CorsConfig.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
         }
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/exception/ThrowUtils.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/exception/ThrowUtils.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/controller/UserController.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/controller/UserController.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/common/BaseResponse.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/common/BaseResponse.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
             outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
             inputPath = new File(inputRootPath, "Dockerfile").getAbsolutePath();
             outputPath = new File(outputRootPath, "Dockerfile").getAbsolutePath();
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/config/JsonConfig.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/config/JsonConfig.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserUpdateRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserUpdateRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/service/UserService.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/service/UserService.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserLoginRequest.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/java/com/yupi/springbootinit/model/dto/user/UserLoginRequest.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
             inputPath = new File(inputRootPath, "src/main/resources/application.yml.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/main/resources/application.yml").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
}
}
