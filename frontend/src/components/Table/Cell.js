import React from 'react';

export const HeaderCell = (props) => {

    const {content} = props;

    return (<th className="capFirstLetter">
            {content}
        </th>)
};

export const DataCell = (props) => {
    const {content} = props;

    return (<td>
        {content}
        {props.children}
    </td>)
};