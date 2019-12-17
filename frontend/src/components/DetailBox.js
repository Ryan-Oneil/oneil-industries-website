import React from "react";

class DetailBox extends React.Component {

    render() {
        const {header} = this.props;
        return (
            <div className="ui raised segments">
                <div className="ui left aligned attached segment textFormat">
                    <h2>{header}</h2>
                </div>
                <div className="ui left aligned attached segment">
                    <div className="textFormat">
                        {this.props.children}
                    </div>
                </div>
            </div>
        )
    }
}
export default DetailBox;