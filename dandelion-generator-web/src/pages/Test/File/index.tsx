import Dragger from "antd/lib/upload/Dragger";
import {InboxOutlined} from "@ant-design/icons";
import {Button, Card, Divider, Flex, Image, message, Typography} from "antd";
import {UploadProps} from "antd/lib";
import React, {useState} from "react";
import {testDownloadUsingGet, testUploadUsingPut} from "@/services/backend/fileController";
import {COS_HOST} from "@/constants";
import {saveAs} from "file-saver";

const fileUpload: React.FC = () => {

  const [value, setValue] = useState<string>('');

  const props: UploadProps = {
    name: 'file',
    multiple: true,
    maxCount: 1,
    customRequest: async (fileObj: any) => {
      try {
        const res = await testUploadUsingPut({}, fileObj.file, {})
        fileObj.onSuccess(res.data)
        // @ts-ignore
        setValue(res.data);
      } catch (e: any) {
        message.error("上传失败" + e.message);
        fileObj.onError(e);
      }
    },
    onRemove() {
      //@ts-ignore
      setValue(undefined);
    }
  };

  return (
    <Flex gap={16}>
      <Card title={"文件上传"}>
        <Dragger {...props}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined/>
          </p>
          <p className="ant-upload-text">Click or drag file to this area to upload</p>
          <p className="ant-upload-hint">
            Support for a single or bulk upload. Strictly prohibited from uploading company data or other
            banned files.
          </p>
        </Dragger>
      </Card>
      <Card loading={!value} title={"文件下载"}>
        <div>文件地址：{COS_HOST + value}</div>
        <Divider/>
        <Image src={COS_HOST + value} height={300}></Image>
        <Divider/>
        <Button onClick={async () => {
          try {
            const blob = await testDownloadUsingGet({key: value}, {
              responseType: "blob"
            })
            const filePath = COS_HOST + value;
            saveAs(blob, filePath.substring(filePath.lastIndexOf("/") + 1))
          } catch (e: any) {
            message.error("下载文件失败")
          }
        }
        }>下载文件</Button>
      </Card>
    </Flex>
  )
}

export default fileUpload;
