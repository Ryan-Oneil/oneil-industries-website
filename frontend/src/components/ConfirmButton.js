import React, { useState } from "react";
import { Button, Modal, Tooltip } from "antd";
import ExclamationCircleOutlined from "@ant-design/icons/lib/icons/ExclamationCircleOutlined";
const { confirm } = Modal;

export default ({
  buttonText,
  buttonIcon,
  confirmAction,
  modalTitle,
  modalDescription,
  toolTip,
  shape
}) => {
  const showDeleteConfirm = () => {
    confirm({
      title: modalTitle,
      icon: <ExclamationCircleOutlined />,
      content: modalDescription,
      okText: "Yes",
      okType: "danger",
      cancelText: "No",
      onOk() {
        confirmAction();
      }
    });
  };

  return (
    <Tooltip title={toolTip}>
      <Button
        icon={buttonIcon}
        onClick={showDeleteConfirm}
        type="primary"
        danger
        shape={shape}
      >
        {buttonText}
      </Button>
    </Tooltip>
  );
};
