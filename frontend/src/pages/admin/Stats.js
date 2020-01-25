import React from "react";
import { connect } from "react-redux";
import { getAdminStats } from "../../actions/admin";
import StatBox from "../../components/StatBox";
import { displayBytesInReadableForm } from "../../functions";
import { ListItem, StatList } from "../../components/StatList";

class Stats extends React.Component {
  componentDidMount() {
    this.props.getAdminStats("/admin/stats");
  }

  renderUserList = () => {
    return this.props.admin.stats.recentUsers.map(user => {
      return (
        <ListItem
          headerText={user.username}
          subText={user.email}
          iconType="user"
          src={require("../../assets/images/userIcon.png")}
        />
      );
    });
  };

  renderFeedBackList = () => {
    return this.props.admin.stats.feedback.map(feedback => {
      return (
        <ListItem
          headerText={feedback.subject}
          subText={feedback.email}
          iconType="email"
          src={require("../../assets/images/email.png")}
        />
      );
    });
  };

  render() {
    const { stats } = this.props.admin;

    return (
      <div className="ui padded grid">
        <div className="row">
          <StatBox header="Total Media" value={stats.totalMedia} />
          <StatBox header="Total Albums" value={stats.totalAlbums} />
          <StatBox header="Total Users" value={stats.totalUsers} />
          <StatBox
            header="Remaining Storage"
            value={displayBytesInReadableForm(stats.remainingStorage * 1024)}
          />
        </div>
        <div className="row">
          <StatList header="Recent Users">
            <div className="ui large divided list">{this.renderUserList()}</div>
          </StatList>
          <StatList header="Recent Feedback">
            <div className="ui large divided list">
              {this.renderFeedBackList()}
            </div>
          </StatList>
        </div>
      </div>
    );
  }
}
const mapStateToProps = state => {
  return { admin: state.admin };
};

export default connect(mapStateToProps, { getAdminStats })(Stats);
