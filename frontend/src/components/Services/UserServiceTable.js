import ServiceTable from "./ServiceTable";
import React from "react";

export default (serviceList, deleteDispatch, service) => {
    return (
        <ServiceTable>
            {serviceList.map(serviceClient => {
                return (
                    <tr className={serviceClient.activated === 1 ? "positive" : "negative"} key={serviceClient.uuid}>
                        <td>{serviceClient.serviceUsername}</td>
                        <td>{serviceClient.uuid}</td>
                        <td>{serviceClient.activated === 1 ? "Enabled" : "Disabled"}</td>
                        <td>
                            <button className="ui icon red button" onClick={() => {deleteDispatch(`/services/user/${service}/delete/`, serviceClient.id, service)}}>
                                <i className="close icon"/>
                            </button>
                        </td>
                    </tr>
                )
            })}
        </ServiceTable>
    )
}