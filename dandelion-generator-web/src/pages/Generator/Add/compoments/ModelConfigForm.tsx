import React from 'react';
import {CloseOutlined} from '@ant-design/icons';
import {Button, Card, Form, FormListFieldData, Input, Space} from 'antd';

interface Props {
  formRef: any;
  oldData: any;
}

const ModelConfigForm: React.FC<Props> = (props) => {
  const {formRef, oldData} = props;

  const singleListView = (field: FormListFieldData, remove?: (index: number | number[]) => void) => {
    return (
      <Space key={field.key}>
        <Form.Item label="字段名称" name={[field.name, 'fieldName']}>
          <Input/>
        </Form.Item>
        <Form.Item label="类型" name={[field.name, 'type']}>
          <Input/>
        </Form.Item>
        <Form.Item label="描述" name={[field.name, 'description']}>
          <Input/>
        </Form.Item>
        <Form.Item label="命令" name={[field.name, 'abbr']}>
          <Input/>
        </Form.Item>
        <Form.Item label="默认值" name={[field.name, 'defaultValue']}>
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
    //如果反转数据顺序，会导致不能正常添加分组
    <Form.List name={['modelConfig', 'models']}>
      {(fields, {add, remove}) => (
        <div style={{display: 'flex', rowGap: 16, flexDirection: 'column'}}>
          {fields.map((field) => {
            const modelConfig =
              formRef?.current?.getFieldsValue()?.modelConfig ?? oldData?.modelConfig;
            console.log(modelConfig)
            const groupKey = modelConfig?.models?.[field.name]?.groupKey;
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
                    <Form.List name={[field.name, 'models']}>
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
            groupKey: 'groupKey',
          })
          } block style={{marginBottom:16}}>
            添加分组
          </Button>
        </div>
      )}
    </Form.List>
  );
};

export default ModelConfigForm;
