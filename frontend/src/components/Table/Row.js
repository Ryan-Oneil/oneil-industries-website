import React from "react";
import {DataCell} from "./Cell";

export const TableRow = (props) => {

    const {rowData, headings} = props;

    const rowCells = Object.keys(rowData).filter(key => headings.includes(key)).map(key => {
        return (
            <DataCell
                key={rowData[key]}
                content={`${rowData[key]}`}
            />
        )
    });

    return (
        <tr>
            {rowCells}
            {props.children}
        </tr>
    )
};