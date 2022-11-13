package swing_frac;

public class moveToMap {
    private int cur;
    private int target;
    public moveToMap(){
        this.cur = 3;
        this.target = 0;
    }
    public int getTarget(){
        return target;
    }
    public int getCur(){
        return cur;
    }
    public boolean CanIGoLeftMap(int cur, int charLocation) {
        // charLocation은  player.getX()임
        this.cur = cur;
        if((cur == 3 || cur == 5) && charLocation == 0) {
            return true;
        }
        else{
            return false;
        }
    }
    public boolean CanIGoRightMap(int cur, int charLocation){
        // charLocation은  player.getX()임
        this.cur = cur;
        if((cur == 1 || cur == 3) && charLocation == 740){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean CanIGoTopMap(int cur, int charLocation){
        // charLocation은  player.getY()임
        this.cur = cur;
        if((cur == 1 || cur == 2 || cur == 5 || cur == 6) && charLocation == 0){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean CanIGoBottomMap(int cur, int charLocation){
        // charLocation은  player.getY()임
        this.cur = cur;
        if((cur == 0 || cur == 1 || cur == 4 || cur == 5) && charLocation == 500){
            return true;
        }
        else{
            return false;
        }
    }
    public void MoveToLeftMap(){
        this.target = 0; // 플레이어가 이동할 맵 인덱스
        switch (cur){
            case 3:
                this.target = 1;
                break;
            case 5:
                this.target = 3;
                break;
            default:
                this.target = cur;
                break;
        }
    }
    public void MoveToRightMap(){
        this.target = 0; // 플레이어가 이동할 맵 인덱스
        switch (cur){
            case 1:
                this.target = 3;
                break;
            case 3:
                this.target = 5;
                break;
            default:
                this.target = cur;
                break;
        }
    }
    public void MoveToTopMap(){
        this.target = 0;
        switch(cur) {
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
                this.target = cur;
                break;
        }
    }
    public void MoveToBottomMap(){
        this.target = 0;
        switch(cur) {
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
                this.target = cur;
                break;
        }
    }
    public static void main(String [] args){
        new moveToMap();
    }
}
