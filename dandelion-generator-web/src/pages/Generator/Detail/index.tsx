import React, {useEffect, useState} from "react";
import {Button, Card, Col, Image, message, Row, Space, Tabs, Tag, Typography} from "antd";
import {useParams} from "@@/exports";
import {getGeneratorVoByIdUsingGet} from "@/services/backend/generatorController";
import {DownloadOutlined, EditOutlined} from "@ant-design/icons";
import moment from "moment";
import FileConfig from "@/pages/Generator/Detail/compenments/FileConfig";
import ModelConfig from "@/pages/Generator/Detail/compenments/ModelConfig";
import AuthorInfo from "@/pages/Generator/Detail/compenments/Author";
import {PageContainer} from "@ant-design/pro-components";
import {downloadGeneratorByIdUsingGet} from "@/services/backend/fileController";
import {saveAs} from "file-saver";
import {Link} from "umi";

const GeneratorDetail: React.FC = () => {
  const {id} = useParams();
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.GeneratorVO>({})

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

  const tagsView = (tags?: string[]) => {
    if (!tags) {
      return <></>
    }
    return tags.map((tag) =>
      <span style={{marginBottom: 16}} key={tag}>
        {<Tag key={tag}>{tag}</Tag>}
      </span>
    )
  }

  const downloadButton = () => {
    return <Button icon={<DownloadOutlined/>} onClick={async () => {
      try {
        // @ts-ignore
        const blob = await downloadGeneratorByIdUsingGet({id}, {
          responseType: "blob"
        })
        const filePath = data.distPath;
        // @ts-ignore
        saveAs(blob, filePath.substring(filePath.lastIndexOf("/") + 1))
      } catch (e: any) {
        message.error("下载文件失败")
      }
    }}>下载</Button>
  }

  const editButton = () => {
    return <Link to={`/generator/update?id=${data.id}`}>
      <Button icon={<EditOutlined/>}>编辑</Button>
    </Link>
  }

  return (
    <PageContainer loading={loading} title={<></>}>
      <Card>
        <Row justify={"space-between"} gutter={[32, 32]}>
          <Col flex={"auto"}>
            <Space size="large" align="center">
              <Typography.Title level={4}>{data.name}</Typography.Title>
              {tagsView(data.tags)}
            </Space>
            <Typography.Paragraph>{data.description}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">
              创建时间：{moment(data.createTime).format('YYYY-MM-DD hh:mm:ss')}
            </Typography.Paragraph>
            <Typography.Paragraph type="secondary">基础包：{data.basePackage}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">版本：{data.version}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">作者：{data.author}</Typography.Paragraph>
            <div style={{marginBottom: 24}}/>
            <Space size="middle">
              <Link to={`/generator/use/${data.id}`}>
                <Button type="primary">立即使用</Button>
              </Link>
              {downloadButton()}
              {editButton()}
            </Space>
          </Col>
          <Col flex="320px">
            <Image src={data.picture}></Image>
          </Col>
        </Row>
      </Card>
      <div style={{marginBottom: 24}}/>
      <Card>
        <Tabs
          size="large"
          defaultActiveKey={'fileConfig'}
          onChange={() => {
          }}
          items={[
            {
              key: 'fileConfig',
              label: '文件配置',
              children: <FileConfig data={data}/>,
            },
            {
              key: 'modelConfig',
              label: '模型配置',
              children: <ModelConfig data={data}/>,
            },
            {
              key: 'userInfo',
              label: '作者信息',
              children: <AuthorInfo data={data}/>,
            },
          ]}
        />
      </Card>
    </PageContainer>
  )
}

export default GeneratorDetail
