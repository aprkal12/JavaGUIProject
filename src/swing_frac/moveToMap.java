package swing_frac;
public class moveToMap {
    private int curMap;
    private int target;
    public moveToMap(int curMap){
        this.curMap = curMap;
        this.target = 0;
    }
    public int getTarget(){
        return target;
    }
    public int getCur(){
        return curMap;
    }
    public boolean CanIGoLeftMap(int curMap, int charLocation) {
        // charLocation은  player.getX()임
        this.curMap = curMap;
        return (curMap == 3 || curMap == 5) && charLocation == 0;
        // 이동 가능 여부에 대한 true/false 리턴
    }
    public boolean CanIGoRightMap(int curMap, int charLocation){
        // charLocation은  player.getX()임
        this.curMap = curMap;
        return (curMap == 1 || curMap == 3) && charLocation == 740;
    }
    public boolean CanIGoTopMap(int curMap, int charLocation){
        // charLocation은  player.getY()임
        this.curMap = curMap;
        return (curMap == 1 || curMap == 2 || curMap == 5 || curMap == 6) && charLocation == 0;
    }
    public boolean CanIGoBottomMap(int curMap, int charLocation){
        // charLocation은  player.getY()임
        this.curMap = curMap;
        return (curMap == 0 || curMap == 1 || curMap == 4 || curMap == 5) && charLocation == 500;
    }
    public void MoveToLeftMap(){
        this.target = 0; // 플레이어가 이동할 맵 인덱스
        switch (curMap){
            case 3:
                this.target = 1;
                break;
            case 5:
                this.target = 3;
                break;
            default:
                this.target = curMap;
                break;
        }
    }
    public void MoveToRightMap(){
        this.target = 0; // 플레이어가 이동할 맵 인덱스
        switch (curMap){
            case 1:
                this.target = 3;
                break;
            case 3:
                this.target = 5;
                break;
            default:
                this.target = curMap;
                break;
        }
    }
    public void MoveToTopMap(){
        this.target = 0;
        switch(curMap) {
            case 1:
                this.target = 0;
                break;
            case 2:
                this.target = 1;
                break;
            case 5:
                this.target = 4;
                break;
            case 6:
                this.target = 5;
                break;
            default:
                this.target = curMap;
                break;
        }
    }
    public void MoveToBottomMap(){
        this.target = 0;
        switch(curMap) {
            case 0:
                this.target = 1;
                break;
            case 1:
                this.target = 2;
                break;
            case 4:
                this.target = 5;
                break;
            case 5:
                this.target = 6;
                break;
            default:
                this.target = curMap;
                break;
        }
    }
}
