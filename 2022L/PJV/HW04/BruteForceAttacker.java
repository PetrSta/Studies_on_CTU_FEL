package cz.cvut.fel.pjv;

public class BruteForceAttacker extends Thief {

    //initialization
    char[] password_letters;
    int password_length;

    //method to start and control recursion
    @Override
    public void breakPassword(int sizeOfPassword) {
        password_letters = getCharacters();
        password_length = sizeOfPassword;
        char[] password_try = new char[sizeOfPassword];
        int password_try_length = 0;

        if(password_length == 0) {
            tryOpen(password_try);
        }

        if(!isOpened()){
            for (char password_letter : password_letters) {
                password_try[password_try_length] = password_letter;
                Recursion(password_try, password_try_length + 1);
                if (isOpened()) {
                    break;
                }
            }
        }
    }

    //recursion itself
    private void Recursion(char[] password_try, int password_try_length){
        if(password_try_length == password_length) {
            tryOpen(password_try);
        } else {
            for (char password_letter : password_letters) {
                password_try[password_try_length] = password_letter;
                if(password_try_length != password_length) {
                    Recursion(password_try, password_try_length + 1);
                } else {
                    Recursion(password_try, password_try_length);
                }
                if(isOpened()) {
                    break;
                }
            }
        }
    }
}
