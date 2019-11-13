import React from 'react';

class ServiceTable extends React.Component {
    render() {
        return (
            <table className="ui celled table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>UUID</th>
                    <th>Status</th>
                    <th>Delete</th>
                </tr>
                </thead>
                <tbody>
                {this.props.children}
                </tbody>
            </table>
        );
    }
}
export default ServiceTable;