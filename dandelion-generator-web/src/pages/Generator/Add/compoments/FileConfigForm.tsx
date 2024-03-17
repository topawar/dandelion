import React from 'react';
import {CloseOutlined} from '@ant-design/icons';
import {Alert, Button, Card, Form, FormListFieldData, Input, Select, Space} from 'antd';

interface Props {
  formRef: any;
  oldData: any;
}

const FileConfigForm: React.FC<Props> = (props) => {
  const {formRef, oldData} = props;

  const singleListView = (field: FormListFieldData, remove?: (index: number | number[]) => void) => {
    return (
      <Space key={field.key}>
        <Form.Item label="输入路径" name={[field.name, 'inputPath']}>
          <Input/>
        </Form.Item>
        <Form.Item label="输出路径" name={[field.name, 'outputPath']}>
          <Input/>
        </Form.Item>
        <Form.Item label="文件类型" name={[field.name, 'type']}>
          <Select options={[
            {value: "file", label: "文件"},
            {value: "dir", label: "目录"}
          ]} style={{minWidth: 100}}>
          </Select>
        </Form.Item>
        <Form.Item label="生成类型" name={[field.name, 'generateType']}>
          <Select options={[
            {value: "static", label: "动态"},
            {value: "generate", label: "静态"}
          ]} style={{minWidth: 100}}>
          </Select>
        </Form.Item>
        <Form.Item label="条件" name={[field.name, 'condition']}>
          <Input/>
        </Form.Item>
        {remove && (
          <Button type="text" danger onClick={() => {
            remove(field.name)
          }}>
            删除
          </Button>
        )}
      </Space>
    )
  }


  // @ts-ignore
  return (
    <>
      <Alert
        message="不需要在线制作，不需要填写"
        type="warning"
        closable
      />
      <div style={{marginBottom: 16}}/>
      {/*如果反转数据顺序，会导致不能正常添加分组*/}
      <Form.List name={['fileConfig', 'files']}>
        {(fields, {add, remove}) => (
          <div style={{display: 'flex', rowGap: 16, flexDirection: 'column'}}>
            {fields.map((field) => {
              const fileConfig =
                formRef?.current?.getFieldsValue()?.fileConfig ?? oldData?.fileConfig;
              const groupKey = fileConfig?.files?.[field.name]?.groupKey;
              return (
                <Card
                  size="small"
                  title={groupKey ? '分组' : '不分组'}
                  key={field.key}
                  extra={
                    <CloseOutlined
                      onClick={() => {
                        remove(field.name);
                      }}
                    />
                  }
                >
                  {groupKey ? (
                    <>
                      <Space>
                        <Form.Item label="分组key" name={[field.name, 'groupKey']}>
                          <Input/>
                        </Form.Item>
                        <Form.Item label="组名" name={[field.name, 'groupName']}>
                          <Input/>
                        </Form.Item>
                        <Form.Item label="类型" name={[field.name, 'type']}>
                          <Input/>
                        </Form.Item>
                        <Form.Item label="条件" name={[field.name, 'condition']}>
                          <Input/>
                        </Form.Item>
                      </Space>
                    </>
                  ) : (
                    singleListView(field)
                  )}
                  {/* Nest Form.List */}
                  {groupKey && (
                    <Form.Item label="组内字段">
                      <Form.List name={[field.name, 'files']}>
                        {(subFields, subOpt) => (
                          <div style={{display: 'flex', flexDirection: 'column', rowGap: 16}}>
                            {subFields.map((subField) => {
                              return singleListView(subField, subOpt.remove)
                            })}
                            <Button type="dashed" onClick={() => subOpt.add()} block>
                              添加字段
                            </Button>
                          </div>
                        )}
                      </Form.List>
                    </Form.Item>
                  )}
                </Card>
              );
            })}

            <Button type="dashed" onClick={() => add()}>
              添加字段
            </Button>
            <Button type="dashed" onClick={() => add({
              groupName: '分组',
              groupKey: 'group',
              type: 'group'
            })
            } block style={{marginBottom: 16}}>
              添加分组
            </Button>
          </div>
        )}
      </Form.List>
    </>
  );
};

export default FileConfigForm;
