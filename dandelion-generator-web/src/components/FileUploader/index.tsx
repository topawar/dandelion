import React, {useState} from "react";
import {Flex, message, UploadFile} from "antd";
import {UploadProps} from "antd/lib";
import {uploadFileUsingPost} from "@/services/backend/fileController";
import Dragger from "antd/lib/upload/Dragger";
import {InboxOutlined} from "@ant-design/icons";
import {COS_HOST} from "@/constants";

interface Props {
  biz: string,
  onChange?: (fileList: UploadFile[]) => void
  value?: UploadFile[],
  description: string
}

const FileUploader: React.FC<Props> = (props) => {
  const {biz, onChange, value, description} = props;
  const [loading, setLoading] = useState(false);
  const uploadProps: UploadProps = {
    name: 'file',
    listType: "text",
    multiple: true,
    maxCount: 1,
    disabled: loading,
    fileList: value,
    onChange: ({fileList}) => {
      onChange?.(fileList)
    },
    customRequest: async (fileObj: any) => {
      try {
        const res = await uploadFileUsingPost({
          biz
        }, {}, fileObj.file)
        fileObj.onSuccess(res.data)
        setLoading(true)
      } catch (e: any) {
        message.error("上传失败" + e.message);
        fileObj.onError(e);
      }
    },
    onRemove() {
      setLoading(false);
    }
  };

  return (
    <Dragger {...uploadProps}>
      <p className="ant-upload-drag-icon">
        <InboxOutlined/>
      </p>
      <p className="ant-upload-text">点击或拖拽文件上传</p>
      <p className="ant-upload-hint">
        {description}
      </p>
    </Dragger>
  )
}

export default FileUploader
