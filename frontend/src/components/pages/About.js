import React from 'react';

const About = () => {
    return (
        <div className="ui container">

            <div className="ui container">
                <h1>Oneil Industries</h1>
                <p className="ui">What started as a random joke on a Garry's Mod server. Now turned into a simplistic friendly community that provides the necessary hosted services to its members.</p>

                <h2>Founding Members</h2>
            </div>
            <table className="ui celled table">

                <thead>
                <tr>
                    <th>Name</th>
                    <th>Title</th>
                </tr>
                </thead>

                <tr>
                    <td>Ryan O'Neil</td>
                    <td>CEO</td>
                </tr>

                <tr>
                    <td>Samusen</td>
                    <td>COO</td>
                </tr>

                <tr>
                    <td>Ady</td>
                    <td>CIO</td>
                </tr>

                <tr>
                    <td>Jack Bushross</td>
                    <td>CBO</td>
                </tr>
            </table>
        </div>
    );
};

export default About;