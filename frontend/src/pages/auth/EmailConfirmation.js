import React, { useEffect, useState } from "react";
import { Button, Card, Result, Steps } from "antd";
import { apiPostCall } from "../../apis/api";
import { Link } from "react-router-dom";
import { getApiError } from "../../apis/ApiErrorHandler";

export default props => {
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    let token = props.match.params.token;

    if (!token) {
      props.history.push("/");
    }
    apiPostCall(`/auth/registrationConfirm/${token}`)
      .then(response => {
        if (response.data) {
          setErrorMessage(response.data);
        } else {
          setSuccessMessage("Your account has been verified");
        }
      })
      .catch(error => setErrorMessage(getApiError(error)));
  }, []);

  return (
    <div style={{ padding: "24px" }}>
      <Card>
        {errorMessage && (
          <Result
            status="error"
            title="Email Confirmation Failed"
            subTitle={errorMessage}
            extra={[
              <Link to="/">
                <Button type="primary" className={""}>
                  Return Home
                </Button>
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
      </Card>
    </div>
  );
};
