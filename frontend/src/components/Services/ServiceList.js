import React from "react";
import { Tree } from "antd";
import { flattenDeep } from "lodash";
import CustomerServiceOutlined from "@ant-design/icons/lib/icons/CustomerServiceOutlined";
import UserOutlined from "@ant-design/icons/lib/icons/UserOutlined";
const { TreeNode } = Tree;

export default ({ serviceList }) => {
  const treeNodes = mapDataToTreeForm(serviceList);

  const getAllKeys = data => {
    const nestedKeys = data.map(node => {
      let childKeys = [];
      if (node.children) {
        childKeys = getAllKeys(node.children);
      }
      return [childKeys, node.key];
    });
    return flattenDeep(nestedKeys);
  };

  return (
    <Tree
      expandedKeys={getAllKeys(serviceList)}
      height={500}
      showIcon={true}
      selectable={false}
      style={{ minHeight: 500 }}
    >
      {treeNodes}
    </Tree>
  );
};

const mapDataToTreeForm = channels => {
  return channels.map(serviceChanel => {
    return (
      <TreeNode
        title={serviceChanel.title}
        key={serviceChanel.key}
        isLeaf={true}
        icon={serviceChanel.title ? <CustomerServiceOutlined /> : ""}
      >
        {renderClients(serviceChanel.usersInChannel)}
        {recursiveTraversal(serviceChanel.children)}
      </TreeNode>
    );
  });
};

const recursiveTraversal = childrenChannels => {
  if (childrenChannels.length > 0) {
    return childrenChannels.map(child => (
      <TreeNode
        title={child.title}
        key={child.key}
        isLeaf={true}
        icon={child.title ? <CustomerServiceOutlined /> : ""}
      >
        {renderClients(child.usersInChannel)}
        {recursiveTraversal(child.children)}
      </TreeNode>
    ));
  }
};

const renderClients = clients => {
  return clients.map(client => {
    return (
      <TreeNode title={client.name} key={client.uuid} icon={<UserOutlined />} />
    );
  });
};
