import React from "react";
import Media from "./Media";

class ApprovalCard extends React.Component {
  render() {
    const {
      uploaderName,
      fileName,
      dateAdded,
      onApproveClick,
      onDeclineClick,
      media
    } = this.props;

    return (
      <div className="ui card">
        <div className="content">
          <Media media={media} onClick={this.props.onClick} />
          <div className="header">{uploaderName}</div>
          <div className="description">
            {fileName} {dateAdded}
          </div>
        </div>
        <div className="extra content">
          <div className="ui two buttons">
            <button className="ui basic green button" onClick={onApproveClick}>
              Approve
            </button>
            <button className="ui basic red button" onClick={onDeclineClick}>
              Decline
            </button>
          </div>
        </div>
      </div>
    );
  }
}
export default ApprovalCard;
