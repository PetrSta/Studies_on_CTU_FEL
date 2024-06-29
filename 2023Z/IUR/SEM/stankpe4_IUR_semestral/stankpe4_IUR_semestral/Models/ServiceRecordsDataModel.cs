using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SemesterWork___car_data_database.Support;

namespace SemesterWork___car_data_database.Models
{
    public class ServiceRecordsDataModel : ViewModelBase
    {
        private bool _editingRecord;
        private bool _stk = false;
        private bool _greenCard = false;
        private bool _serviceFinished = false;
        private string _serviceNotes = string.Empty;
        private string _recordTag = string.Empty;
        private DateTime _date = DateTime.Now;
        private int? _price;
        private RelayCommand _okCommand;
        private ServiceRecordsViewModel _serviceRecordsViewModel;

        public ServiceRecordsDataModel() { }

        public bool EditingRecord
        {
            get { return _editingRecord; }
            set
            {
                _editingRecord = value;
                OnPropertyChanged(nameof(EditingRecord));
            }

        }

        public bool STK
        {
            get { return _stk; }
            set
            {
                _stk = value;
                OnPropertyChanged(nameof(STK));
            }
        }

        public bool GreenCard
        {
            get { return _greenCard; }
            set
            {
                _greenCard = value;
                OnPropertyChanged(nameof(GreenCard));
            }
        }

        public string ServiceNotes
        {
            get { return _serviceNotes; }
            set
            {
                _serviceNotes = value;
                OnPropertyChanged(nameof(ServiceNotes));
            }
        }

        public bool ServiceFinished
        {
            get { return _serviceFinished; }
            set
            {
                _serviceFinished = value;
                OnPropertyChanged(nameof(ServiceFinished));
            }
        }

        public string RecordTag
        {
            get { return _recordTag; }
            set
            {
                _recordTag = value;
                OnPropertyChanged(nameof(RecordTag));
            }
        }

        public string Date
        {
            // convert date time to string
            get 
            {
                string day = _date.Day.ToString();
                string month = _date.Month.ToString();
                string year = _date.Year.ToString();
                string finalDateFormat = day + "." + month + "." + year;
                return finalDateFormat; 
            }
            // covert string to date time
            set
            {
                try
                {
                    _date = DateTime.Parse(value);
                }
                catch
                {
                    _date = DateTime.Now;
                }
                OnPropertyChanged(nameof(Date));
            }
        }

        public int? Price
        {
            get { return _price; }
            set
            {
                _price = value;
                OnPropertyChanged(nameof(Price));
            }
        }

        public ServiceRecordsViewModel ServiceRecordsViewModel
        {
            get { return _serviceRecordsViewModel; }
            set
            {
                _serviceRecordsViewModel = value;
                OnPropertyChanged(nameof(ServiceRecordsViewModel));
            }
        }

        public RelayCommand OkCommand
        {
            get
            {
                return _okCommand ?? (_okCommand = new RelayCommand(EndDialogWindow_OK, OkCommandCanExecute));
            }
        }

        private bool OkCommandCanExecute(object obj)
        {
            // check if we can create record with everything we need
            if(RecordTag != string.Empty && DateTime.Parse(Date).Year >= 1886)
            {
                return true;
            }
            return false;
        }

        private void EndDialogWindow_OK(object obj)
        {
            if(!EditingRecord)
            {
                // adding new record
                ServiceRecordsViewModel.ServiceList.Add(this);
            }
        }
    }
}
