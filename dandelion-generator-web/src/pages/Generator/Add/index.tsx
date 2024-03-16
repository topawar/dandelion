import {
  ProCard,
  ProFormInstance,
  ProFormItem,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm
} from '@ant-design/pro-components';
import {message, UploadFile} from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import PictureUploader from "@/components/PictureUploader";
import FileUploader from "@/components/FileUploader";
import {
  addGeneratorUsingPost,
  editGeneratorUsingPost,
  getGeneratorVoByIdUsingGet
} from "@/services/backend/generatorController";
import {history} from "@umijs/max";
import {useSearchParams} from "@@/exports";
import {COS_HOST} from "@/constants";
import ModelConfigForm from "@/pages/Generator/Add/compoments/ModelConfigForm";

/**
 * 添加数据
 * @param values
 */

const GeneratorAddPage: React.FC = () => {
  const formRef = useRef<ProFormInstance>();
  const [searchParams] = useSearchParams();
  //@ts-ignore
  const id: number = searchParams.get("id");
  const [oldData, setOldData] = useState<API.GeneratorEditRequest>();

  /**
   * 新建配置
   * @param values
   */
  const doAdd = async (values: API.GeneratorAddRequest) => {
    try {
      const res = await addGeneratorUsingPost(values);
      if (res.data) {
        message.success("创建成功")
        history.push(`/generator/detail/${res.data}`)
      }
    } catch (e: any) {
      message.error("添加数据异常" + e.message)
    }
  }

  /**
   * 修改配置
   * @param values
   */
  const doEdit = async (values: API.GeneratorEditRequest) => {
    try {
      const res = await editGeneratorUsingPost(values);
      if (res.data) {
        message.success("修改成功")
        history.push(`/generator/detail/${id}`)
      }
    } catch (e: any) {
      message.error("更爱数据异常" + e.message)
    }
  }

  const doSubmit = async (values: API.GeneratorAddRequest | API.GeneratorEditRequest) => {
    //处理fileConfig
    if (!values.fileConfig) {
      values.fileConfig = {}
    }

    //处理modelConfig
    if (!values.modelConfig) {
      values.modelConfig = {}
    }

    //文件列表转换为url
    if (values.distPath && values.distPath.length > 0) {
      //@ts-ignore
      values.distPath = values.distPath[0].response;
    }
    if (id) {
      await doEdit({
        id,
        ...values
      });
    } else {
      await doAdd(values);
    }
  }

  const loadData = async () => {
    //如果id不存在直接返回
    if (!id) {
      return
    }
    try {
      const res = await getGeneratorVoByIdUsingGet({id});
      if (res) {
        //处理文件路径
        const {distPath} = res.data ?? {}
        if (distPath) {
          //@ts-ignore
          res.data.distPath = [{
            uid: id,
            name: '文件' + id,
            status: 'done',
            url: COS_HOST + distPath,
            response: distPath
          } as UploadFile
          ]
        }
        setOldData(res.data)
      }
    } catch (e: any) {
      message.error("获取数据异常" + e.message)
    }
  }
  useEffect(() => {
    if (id) {
      loadData()
    }
  }, [id])

  return (
    <ProCard>
      {}
      {(!id || oldData) && <StepsForm<API.GeneratorAddRequest | API.GeneratorEditRequest>
        formRef={formRef}
        onFinish={doSubmit}
        formProps={{
          initialValues: oldData,
        }}
      >
        <StepsForm.StepForm<{
          name: string;
        }>
          name="base"
          title="基本信息"
          stepProps={{
            description: '生成器基本信息',
          }}
          onFinish={async () => {
            console.log(formRef.current?.getFieldsValue());
            return true;
          }}
        >
          <ProFormText
            name="name"
            label="名称"
            width="md"
            placeholder="请输入名称"
            rules={[{required: true}]}
          />
          <ProFormTextArea
            name="description"
            label="描述"
            width="lg"
            placeholder="请输入备注"
          />
          <ProFormText name="basePackage" label="包名"></ProFormText>
          <ProFormText name="version" label="版本号"></ProFormText>
          <ProFormText name="author" label="作者" placeholder="请输入作者"/>
          <ProFormSelect name="tags" mode={"tags"} label="标签"></ProFormSelect>
          <ProFormItem name={"picture"} label={"封面"}>
            <PictureUploader biz={"user_avatar"}></PictureUploader>
          </ProFormItem>
        </StepsForm.StepForm>
        <StepsForm.StepForm name="fileConfig" title="文件配置">
          {/*todo 待补充*/}
        </StepsForm.StepForm>
        <StepsForm.StepForm name="modelConfig" title="模型配置" onFinish={async ()=>{
          return true;
        }}>
          <ModelConfigForm formRef={formRef} oldData={oldData}></ModelConfigForm>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          title="产物包"
        >
          <ProFormItem name="distPath">
            <FileUploader biz={"generator_picture"} description={"请上传压缩文件"}></FileUploader>
          </ProFormItem>
        </StepsForm.StepForm>
      </StepsForm>}
    </ProCard>
  );
};

export default GeneratorAddPage
