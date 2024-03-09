import {PageContainer, ProFormSelect, ProFormText, QueryFilter} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {Avatar, Card, Flex, List, message, Tabs, TabsProps} from "antd";
import {listGeneratorVoByPageUsingPost} from "@/services/backend/generatorController";
import Search from "antd/es/input/Search";
import dayjs from "dayjs";
import Paragraph from "antd/lib/typography/Paragraph";

const DEFAULT_PAGE_PARAMS = {
  current: 1,
  pageSize: 4,
  sortField: "createTime",
  sortOrder: "descend"
}


const Index: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [searchParams, setsearchParams] = useState<API.GeneratorQueryRequest>({
    ...DEFAULT_PAGE_PARAMS
  });
  const [dataList, setDataList] = useState<API.GeneratorVO>([]);
  const [total, setTotal] = useState<number>(0);
  const doSearch = async () => {
    try {
      const res = await listGeneratorVoByPageUsingPost(searchParams);
      setDataList(res.data?.records ?? [])
      setTotal(res.data?.total ?? 0)
    } catch (e: any) {
      message.error("获取数据列表异常" + e.message)
      setLoading(false)
    }
    setLoading(false)
  }
  useEffect(() => {
    doSearch();
  }, [searchParams])
  return (
    <PageContainer>
      <Search
        allowClear
        enterButton="搜索"
        size="large"
        onSearch={() => {

        }}
      />
      <Tabs
        defaultActiveKey="1"
        items={[
          {
            label: '新发布',
            key: '1',
          },
          {
            label: '推荐',
            key: '2',
          },
        ]}
      />
      <QueryFilter defaultCollapsed labelAlign={"left"} span={12} labelWidth={"auto"}>
        <ProFormText name="name" label="名称"/>
        <ProFormText name="description" label="描述"/>
        <ProFormSelect name="tags" label="标签" mode={"tags"}/>
      </QueryFilter>

      <List<API.GeneratorVO>
        rowKey="id"
        loading={loading}
        grid={{
          gutter: 16,
          xs: 1,
          sm: 2,
          md: 3,
          lg: 3,
          xl: 4,
          xxl: 4,
        }}
        dataSource={dataList}
        renderItem={(item) => (
          <List.Item>
            <Card hoverable cover={<img alt={item.picture} src={item.picture} style={{height: 230}}/>}>
              <Card.Meta
                title={<a>{item.name}</a>}
                description={
                  <Paragraph
                    ellipsis={{
                      rows: 2,
                    }}
                    style={{height: 44}}
                  >
                    {item.description}
                  </Paragraph>
                }
              />
              <Flex justify={"space-between"} align={"center"}>
                <span>{dayjs(item.createTime).format("YYYY/MM/DD")}</span>
                <div>
                  <Avatar src={item.userVO?.userAvatar}></Avatar>
                </div>
              </Flex>
            </Card>
          </List.Item>
        )}
      />
    </PageContainer>
  );
};
export default Index;
