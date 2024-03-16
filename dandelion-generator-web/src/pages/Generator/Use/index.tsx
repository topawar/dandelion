import React, {useEffect, useState} from "react";
import {Button, Card, Col, Collapse, Form, Image, Input, message, Row, Space, Typography} from "antd";
import {useParams} from "@@/exports";
import {getGeneratorVoByIdUsingGet, useGeneratorUsingPost} from "@/services/backend/generatorController";
import {PageContainer} from "@ant-design/pro-components";
import {Link} from "umi";
import {DownloadOutlined} from "@ant-design/icons";
import {saveAs} from "file-saver";

const GeneratorUse: React.FC = () => {
  const {id} = useParams();
  const [loading, setLoading] = useState(false);
  const [downloading, setDownLoading] = useState(false);
  const [data, setData] = useState<API.GeneratorVO>({})
  const [form] = Form.useForm();
  const models = data?.modelConfig?.models ?? [];

  const loadData = async () => {
    setLoading(true)
    //如果id不存在直接返回
    if (!id) {
      return
    }
    try {
      //@ts-ignore
      const res = await getGeneratorVoByIdUsingGet({id});
      if (res) {
        setData(res.data || {})
      }
    } catch (e: any) {
      message.error("获取数据异常" + e.message)
    }
    setLoading(false)
  }
  useEffect(() => {
    if (id) {
      loadData()
    }
  }, [id])

  const downloadButton =
    (
      <Button
        type={"primary"}
        icon={<DownloadOutlined/>}
        loading={downloading}
        onClick={async () => {
          setDownLoading(true)
          try {
            const values = form.getFieldsValue();
            // eslint-disable-next-line react-hooks/rules-of-hooks
            const blob = await useGeneratorUsingPost(
              {
                dataModel: values,
                id: data.id
              }, {
                responseType: "blob"
              })
            // @ts-ignore
            const fullPath = data.distPath || '';
            saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1));
            setDownLoading(false)
          } catch (e: any) {
            message.error("下载文件失败")
          }
        }}>下载</Button>
    )

  // @ts-ignore
  return (
    <PageContainer loading={loading} title={<></>}>
      <Card>
        <Row justify={"space-between"} gutter={[32, 32]}>
          <Col flex={"auto"}>
            <Space size="large" align="center">
              <Typography.Title level={4}>{data.name}</Typography.Title>
            </Space>
            <Form form={form}>
              {
                models.map((model, index) => {
                  if (model.groupKey) {
                    if (!model.models) {
                      return <></>;
                    }
                    return (
                      <Collapse key={index} style={{marginBottom: 24}} items={[
                        {
                          key: index,
                          label: model.groupName + '(分组)',
                          children: model.models.map((subModel, index) => {
                            return (
                              <Form.Item key={index} label={subModel.fieldName}
                                //@ts-ignore
                                         name={[model.groupKey, subModel.fieldName]}>
                                <Input placeholder={subModel.description}></Input>
                              </Form.Item>
                            )
                          }),
                        }]}
                                bordered={false}
                                defaultActiveKey={[index]}
                      />
                    )
                  }
                  return (
                    <Form.Item key={index} label={model.fieldName}
                      //@ts-ignore
                               name={[model.groupKey, model.fieldName]}>
                      <Input placeholder={model.description}></Input>
                    </Form.Item>
                  )
                })
              }
            </Form>
            <div style={{marginBottom: 24}}/>
            <Space size="middle">
              {downloadButton}
              <Link to={`/generator/detail/${data.id}`}>
                <Button>查看详情</Button>
              </Link>
            </Space>
          </Col>
          <Col flex="320px">
            <Image src={data.picture}></Image>
          </Col>
        </Row>
      </Card>
    </PageContainer>
  )
}

export default GeneratorUse
