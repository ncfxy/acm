#include <cstdio>
#include <memory.h>
#include <string.h>

typedef struct{
    int left,right;
    int sum;
}TreeNode;

class NcfxySegmentTree{ // �߶���
    private:
        TreeNode* nodes;
        int* data;
    public:
        NcfxySegmentTree(TreeNode* nodes, int* data){
            this->nodes = nodes;
            this->data = data;
        }
        void build(int nodeIndex, int begin, int end){//�����߶���
            if(begin == end){// ֻ��һ��Ԫ��ֱ�ӱ�����Ϣ
                nodes[nodeIndex].left = nodes[nodeIndex].right = begin;
                nodes[nodeIndex].sum = data[begin];
            }else{
                // �ݹ鹹����������
                build(2*nodeIndex, begin, (begin+end)/2);
                build(2*nodeIndex+1, (begin+end)/2+1, end);

                // ���ݵõ���ǰ�ڵ����Ϣ
                nodes[nodeIndex].left = begin;
                nodes[nodeIndex].right = end;
                nodes[nodeIndex].sum = nodes[2*nodeIndex].sum + nodes[2*nodeIndex+1].sum;
            }
        }

        int query(int nodeIndex, int begin, int end){
            int p1, p2;
            // ��ѯ�����Ҫ�������û�н���
            if(nodes[nodeIndex].left > end || nodes[nodeIndex].right < begin)
                return -1;
            // ����ڵ���������ڲ�ѯ������ֱ�ӷ���
            if(begin <= nodes[nodeIndex].left && end >= nodes[nodeIndex].right)
                return nodes[nodeIndex].sum;

            p1 = query(nodeIndex*2, begin, end);
            p2 = query(nodeIndex*2+1, begin, end);

            if(p1 == -1)return p2;
            if(p2 == -1)return p1;
            return p1 + p2;
        }

        void update(int nodeIndex, int changeIndex, int value){
            if(nodes[nodeIndex].left == nodes[nodeIndex].right){
                nodes[nodeIndex].sum += value;
                return;
            }
            int m = (nodes[nodeIndex].left + nodes[nodeIndex].right) >> 1;
            if(changeIndex <= m)update(nodeIndex*2, changeIndex,value);
            else update(nodeIndex*2+1,changeIndex,value);

            // ���ݸ��¸��ڵ�
            nodes[nodeIndex].sum += value;
        }


};

const int M = 50005;
int n,t,array[M];
TreeNode nodes[M*4];

void hdu1166(){
    char str[10];
    while(scanf("%d",&t)!=EOF){
        for(int i = 1;i <= t;i++){
            scanf("%d",&n);
            for(int j = 1;j <= n;j++){
                scanf("%d",&array[j]);
            }
            NcfxySegmentTree bit(nodes,array);
            bit.build(1,1,n);

            printf("Case %d:\n",i);
            while(scanf("%s",&str)!=EOF){
                int a,b;
                if(str[0] == 'A'){
                    scanf("%d%d",&a,&b);
                    bit.update(1,a,b);
                }else if(str[0] == 'S'){
                    scanf("%d%d",&a,&b);
                    bit.update(1,a,-b);
                }else if(str[0] == 'Q'){
                    scanf("%d%d",&a,&b);
                    printf("%d\n",bit.query(1,a,b));
                }else{
                    break;
                }
            }
        }
    }
}

int main(){
    //hdu1541();
    hdu1166();
    return 0;
}
