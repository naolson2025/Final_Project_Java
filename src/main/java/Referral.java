public class Referral {

    public static void main(String[] args) {
        Referral referral = new Referral();
        referral.start();
    }

    public void start(){
        fraudReferralGUI gui = new fraudReferralGUI(this);
    }

}
