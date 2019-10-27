import React from "react";

export const renderInput = ( {input, label, meta, type}) => {
    const className = `field ${meta.error && meta.touched ? `error` : ``}`;

    return (
        <div className={className}>
            <input {...input} autoComplete="off" type={type} placeholder={label}/>
            {renderError(meta)}
        </div>
    );
};

export const renderIconInput = ( {input, label, meta, iconType, type}) => {
    const className = `field ${meta.error && meta.touched ? `error` : ``}`;
    const iconClassName = `${iconType} icon`;

    return (
        <div className={className}>
            <div className="ui left icon input">
                <i className={iconClassName}/>
                <input {...input} autoComplete="off" type={type} placeholder={label}/>
            </div>
            {renderError(meta)}
        </div>
    );
};

export const renderFileField = ({ input, type, meta}) => {
    delete input.value;

    const className = `field ${meta.error && meta.touched ? `error` : ``}`;

    return (
        <div className={className}>
            <input {...input} type={type}/>
            {renderError(meta)}
        </div>
    )
};

export const renderError =({error, touched}) => {
    if (touched && error) {
        return (
            <div className="ui error message">
                {<div className="header">{error}</div>}
            </div>
        );
    }
};