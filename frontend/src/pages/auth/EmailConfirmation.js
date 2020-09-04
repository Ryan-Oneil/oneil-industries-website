import React, { useEffect, useState } from "react";
import { Button, Result } from "antd";
import { apiPostCall } from "../../apis/api";
import { getApiError } from "../../helpers";
import { Link } from "react-router-dom";

export default props => {
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    let token = props.match.params.token;

    if (!token) {
      props.history.push("/");
    }
    apiPostCall(`/user/registrationConfirm/${token}`)
      .then(response => setSuccessMessage(response.data))
      .catch(error => setErrorMessage(getApiError(error)));
  }, []);

  return (
    <>
      {errorMessage && (
        <Result
          status="error"
          title="Email Confirmation Failed"
          subTitle={errorMessage}
          extra={[
            <Link to="/">
              <Button type="primary">Return Home</Button>
            </Link>
          ]}
        />
      )}

      {successMessage && (
        <Result
          status="success"
          title="Email Successfully Confirmed"
          subTitle={successMessage}
          extra={[
            <Link to="/login">
              <Button type="primary">Login</Button>
            </Link>
          ]}
        />
      )}
    </>
  );
};
