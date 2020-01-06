package longse.com.learing.utils.binarytree;

public class TreeNode {
    public int key = 0;
    public String data = null;
    public boolean isVisted = false;
    public TreeNode leftChild = null;
    public TreeNode rightChild = null;

    public TreeNode() {
    }

    /**
     * @param key  层序编码
     * @param data 数据域
     */
    public TreeNode(int key, String data) {
        this.key = key;
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
    }
}
