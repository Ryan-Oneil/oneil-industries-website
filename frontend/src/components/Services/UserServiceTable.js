import ServiceTable from "./ServiceTable";
import React from "react";

export default (header, serviceList, deleteDispatch) => {
    return (
        <div>
            <h2 className="ui centered header">{header}</h2>
            <ServiceTable>
                {serviceList.map(serviceClient => {
                    return (
                        <tr className={serviceClient.activated === 1 ? "positive" : "negative"} key={serviceClient.uuid}>
                            <td>{serviceClient.serviceUsername}</td>
                            <td>{serviceClient.uuid}</td>
                            <td>{serviceClient.activated === 1 ? "Enabled" : "Disabled"}</td>
                            <td>
                                <button className="ui icon red button" onClick={() => {deleteDispatch(`/user/profile/service/${header.toLowerCase()}/delete/`, serviceClient.id, header.toLowerCase())}}>
                                    <i className="close icon"/>
                                </button>
                            </td>
                        </tr>
                    )
                })}
            </ServiceTable>
        </div>
    )
}