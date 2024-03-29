import {PageContainer, ProFormSelect, ProFormText, QueryFilter} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {Avatar, Card, Flex, List, message, Tabs, Tag} from "antd";
import {
  listGeneratorVoByPageFastUsingPost,
  listGeneratorVoByPageUsingPost
} from "@/services/backend/generatorController";
import Search from "antd/es/input/Search";
import dayjs from "dayjs";
import Paragraph from "antd/lib/typography/Paragraph";
import {Link} from "umi";

const DEFAULT_PAGE_PARAMS = {
  current: 1,
  pageSize: 12,
  sortField: "createTime",
  sortOrder: "descend"
}


const Index: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [searchParams, setsearchParams] = useState<API.GeneratorQueryRequest>({
    ...DEFAULT_PAGE_PARAMS
  });
  const [dataList, setDataList] = useState<API.GeneratorVO[]>([]);
  const [total, setTotal] = useState<number>(0);
  const doSearch = async () => {
    try {
      const res = await listGeneratorVoByPageFastUsingPost(searchParams);
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

  /**
   * 标签列表
   * @param tags
   */
  const tagsView = (tags?: string[]) => {
    if (!tags || !Array.isArray(tags)) {
      return <></>;
    }

    return tags.map((tag) =>
      <span style={{marginBottom: 16}} key={tag}>
        {<Tag key={tag}>{tag}</Tag>}
      </span>
    )
  }
  // @ts-ignore
  return (
    <PageContainer title={<></>}>
      <Flex justify={"center"}>
        <Search
          allowClear
          enterButton="搜索"
          size="large"
          onSearch={(value) => {
            setsearchParams({
              ...DEFAULT_PAGE_PARAMS,
              ...searchParams,
              searchText: value
            })
          }}
        />
      </Flex>
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
      <QueryFilter defaultCollapsed={false} labelAlign="left" span={12} labelWidth={"auto"}
                   style={{padding: '16px 0' }}
                   onFinish={async (values: API.GeneratorQueryRequest) => {
                     setsearchParams({
                       ...DEFAULT_PAGE_PARAMS,
                       ...values,
                       searchText: searchParams.searchText
                     })
                   }}>
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
        pagination={{
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total,
          onChange(current: number, pageSize: number) {
            setsearchParams({
              ...searchParams,
              current,
              pageSize,
            });
          },
        }}
        renderItem={(item) => (
            <List.Item>
              <Link to={`/generator/detail/${item.id}`}>
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
                {tagsView(item.tags)}
                <Flex justify={"space-between"} align={"center"}>
                  <span>{dayjs(item.createTime).format("YYYY/MM/DD")}</span>
                  <div>
                    <Avatar src={item.userVO?.userAvatar}></Avatar>
                  </div>
                </Flex>
              </Card>
              </Link>
            </List.Item>
        )}
      />
    </PageContainer>
  );
};
export default Index;
