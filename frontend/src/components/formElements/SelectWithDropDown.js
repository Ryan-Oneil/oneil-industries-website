import React, { useState } from "react";
import { Button, Divider, Input, Select } from "antd";
import PlusOutlined from "@ant-design/icons/lib/icons/PlusOutlined";
const { Option } = Select;

export default ({
  defaultValue,
  placeHolder,
  onChange,
  onSubmit,
  optionValues
}) => {
  const [inputValue, setInputValue] = useState("");

  return (
    <Select
      style={{ width: "100%" }}
      size={"large"}
      placeholder={placeHolder}
      defaultValue={defaultValue}
      onChange={onChange}
      dropdownRender={menu => (
        <>
          {menu}
          <Divider style={{ margin: "4px 0" }} />
          <div style={{ display: "flex", flexWrap: "nowrap", padding: 8 }}>
            <Input
              style={{ flex: "auto" }}
              value={inputValue}
              onChange={event => setInputValue(event.target.value)}
            />
            <Button
              onClick={() => onSubmit(inputValue)}
              disabled={inputValue.length === 0}
            >
              <PlusOutlined />
            </Button>
          </div>
        </>
      )}
    >
      {optionValues.map(item => (
        <Option key={item.id} value={item.id}>
          {item.name}
        </Option>
      ))}
    </Select>
  );
};
