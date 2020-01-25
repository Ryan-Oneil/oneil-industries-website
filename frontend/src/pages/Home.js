import React from "react";

const Home = () => {
  return (
    <div className="ui marginPadding whiteText">
      <h1 className="ui centered inverted header">
        Oneil Industries a community dedicated to banter
      </h1>

      <img
        src={require("../assets/images/oneilFactory.png")}
        className="ui image centerVideo"
        alt="Factory"
      />

      <h4 className="ui horizontal inverted divider header">
        <i className="tag icon" />
        Some of the things that makes us great
      </h4>

      <div className="ui center aligned grid marginPadding">
        <div className="four wide column leftAlignText formatInfo">
          <h3>
            <i className="headphones icon" />
            Voice Services
          </h3>
          <div>
            We provide whitelisted Teamspeak and Discord servers for all to use.
            Navigate to the services section of the website to connect to them.
          </div>
        </div>
        <div className="four wide column leftAlignText formatInfo">
          <h3>
            <i className="wrench icon" />
            Tools
          </h3>
          <div>
            Oneil Industries provides a wide variety of tools to its Members
            such as image gallery, game calculators, and more. All found under
            the dashboard section for registered users.
          </div>
        </div>
        <div className="four wide column leftAlignText formatInfo">
          <h3>
            <i className="server icon" />
            Game Servers
          </h3>
          <div>
            Game servers are spun up for the times we want to play games
            together that require servers such as survival games.
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
