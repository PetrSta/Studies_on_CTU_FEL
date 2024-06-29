using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IUR_P04_homework_template
{
    class ProfileData : ProfileDataBase
    {
        private string _forename = string.Empty;
        private string _surename = string.Empty;
        private string _eMail = string.Empty;

        public ProfileData() { }
        public string Forename
        {
            get { return _forename; }
            set 
            { 
                _forename = value; 
                OnPropertyChanged(nameof(Forename));
            }
        }

        public string Surename
        {
            get { return _surename; }
            set
            {
                _surename = value; 
                OnPropertyChanged(nameof(Surename));
            }
        }

        public string Email
        {
            get { return _eMail; }
            set
            {
                _eMail = value;
                OnPropertyChanged(nameof(Email));
            }
        }
    }
}
