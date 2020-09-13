import { Button, Modal } from "antd";
import React from "react";
import { useDispatch } from "react-redux";
import { clearError, disableError } from "../reducers/globalErrorReducer";

export default props => {
  const { message } = props;
  const dispatch = useDispatch();

  const closeModal = () => {
    dispatch(clearError());
  };

  return (
    <Modal
      visible
      onCancel={closeModal}
      onOk={closeModal}
      title="An Error Occurred"
      footer={[
        <Button key="disable" onClick={() => dispatch(disableError())}>
          Disable
        </Button>,
        <Button
          key="confirm"
          type="primary"
          onClick={() => dispatch(clearError())}
        >
          Ok
        </Button>
      ]}
    >
      <p>{message}</p>
    </Modal>
  );
};
