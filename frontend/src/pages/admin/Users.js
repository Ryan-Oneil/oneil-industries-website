import React from "react";
import { connect } from "react-redux";
import { getUsers } from "../../actions/admin";
import { Link } from "react-router-dom";
import { SearchAbleTable } from "../../components/Table/SearchAbleTable";

class Users extends React.Component {
  constructor(props) {
    super(props);
    this.props.getUsers("/admin/users");
  }

  renderUserRows = rows => {
    return rows.map(row => {
      return {
        username: row.username,
        email: row.email,
        role: row.role,
        enabled: row.enabled ? "Enabled" : "Disabled",
        manage: row.manage
      };
    });
  };

  render() {
    const columns = [
      {
        Header: "Username",
        accessor: "username"
      },
      {
        Header: "Email",
        accessor: "email"
      },
      {
        Header: "Role",
        accessor: "role"
      },
      {
        Header: "Enabled",
        accessor: "enabled"
      },
      {
        Header: "Manage",
        Cell: ({ row }) => (
          <Link to={`users/${row.values.username}`}>
            <i className="edit icon" />
            Manage
          </Link>
        )
      }
    ];
    const { users } = this.props.admin;

    return (
      <div className="ui padded grid">
        <div className="thirteen wide column">
          <SearchAbleTable
            columns={columns}
            data={this.renderUserRows(users)}
          />
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { admin: state.admin };
};

export default connect(mapStateToProps, { getUsers })(Users);
