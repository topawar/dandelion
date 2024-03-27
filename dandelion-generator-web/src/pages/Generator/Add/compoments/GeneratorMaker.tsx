import FileUploader from '@/components/FileUploader';
import { generatorMakerUsingPost } from '@/services/backend/generatorController';
import { ProForm, ProFormItem } from '@ant-design/pro-form';
import { Collapse, Form, message } from 'antd';
import { saveAs } from 'file-saver';
import {ProFormInstance} from "@ant-design/pro-components";
import {useRef} from "react";

interface Props {
  meta: API.GeneratorAddRequest | API.GeneratorEditRequest;
}

/**
 * 生成器制作
 * @param props
 */
export default (props: Props) => {
  const { meta } = props;
  const formRef = useRef<ProFormInstance>();

/**
 * 提交
 * @param values
 */
const doSubmit = async (values: API.GeneratorMakerRequest) => {
  // 校验
  if (!meta.name) {
    message.error('请填写名称');
    return;
  }

  console.log(values)

  const zipFilePath = values.zipPath;
  if (!zipFilePath || zipFilePath.length < 1) {
    message.error('请上传模板文件压缩包');
    return;
  }

  // 文件列表转 url
  // @ts-ignore
  values.zipPath = zipFilePath[0].response;

  try {
    const blob = await generatorMakerUsingPost(
      {
        meta,
        zipPath: values.zipPath,
      },
      {
        responseType: 'blob',
      },
    );
    // 使用 file-saver 来保存文件
    saveAs(blob, meta.name + '.zip');
  } catch (error: any) {
    message.error('下载失败，' + error.message);
  }
};

/**
 * 表单视图
 */
const formView = (
  <ProForm
    formRef={formRef}
    submitter={{
      searchConfig: {
        submitText: '制作',
      },
      resetButtonProps: {
        hidden: true,
      },
    }}
    onFinish={doSubmit}
  >
    <ProFormItem label="模板文件" name="zipPath">
      <FileUploader
        biz="generator_make_template"
        description="请上传压缩包，打包时不要添加最外层目录！"
      />
    </ProFormItem>
  </ProForm>
);

return (
  <Collapse
    style={{
      marginBottom: 24,
    }}
    items={[
      {
        key: 'maker',
        label: '生成器制作工具',
        children: formView,
      },
    ]}
  />
);
};
