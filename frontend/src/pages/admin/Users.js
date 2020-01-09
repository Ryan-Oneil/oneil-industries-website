import React from "react";
import { connect } from "react-redux";
import SearchAbleTable from "../../components/Table/SearchAbleTable";
import { getUsers } from "../../actions/admin";
import { TableRow } from "../../components/Table/Row";
import { DataCell } from "../../components/Table/Cell";
import { Link } from "react-router-dom";

class Users extends React.Component {
  constructor(props) {
    super(props);
    this.props.getUsers("/admin/users");
  }

  renderUserRows = (rows, headings) => {
    return rows.map(row => {
      return (
        <TableRow rowData={row} headings={headings} key={row.id}>
          <DataCell>
            <Link to={`users/${row.username}`}>
              <i className="edit icon" />
              Manage
            </Link>
          </DataCell>
        </TableRow>
      );
    });
  };

  render() {
    const tableHeaders = ["username", "email", "role", "enabled", "Manage"];
    const { users } = this.props.admin;

    return (
      <div className="ui padded grid">
        <div className="thirteen wide column">
          {users && (
            <SearchAbleTable
              headings={tableHeaders}
              rows={this.renderUserRows(users, tableHeaders)}
            />
          )}
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { admin: state.admin };
};

export default connect(mapStateToProps, { getUsers })(Users);
