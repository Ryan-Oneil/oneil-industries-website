import React from 'react';
import {HeaderCell} from "./Cell";

class SearchAbleTable extends React.Component {

    renderTableHeaders =(_cell, cellIndex) => {
        const {headings} = this.props;

        return (
            <HeaderCell
                key={cellIndex}
                content={headings[cellIndex]}
            />
        )
    };

    render() {
        const {headings, rows} = this.props;

        this.renderTableHeaders = this.renderTableHeaders.bind(this);

        const tableHeadings = (
            <tr key="heading">
                {headings.map(this.renderTableHeaders)}
            </tr>
        );
        return (
            <table className="ui large table">
                <thead>{tableHeadings}</thead>
                <tbody>{rows}</tbody>
            </table>
        );
    }
}
export default SearchAbleTable;