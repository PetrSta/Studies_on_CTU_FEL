using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using SemesterWork___car_data_database.Support;

namespace SemesterWork___car_data_database.Models
{
    public class FuelRecordsDataModel : ViewModelBase
    {
        private bool _editingRecord;
        private float? _amountRefueled = 0;
        private float? _distanceTraveled = 0;
        private int? _price = null;
        private DateTime _date = DateTime.Now;
        private string _additionNote = string.Empty;
        private RelayCommand _okCommand;
        private FuelRecordsViewModel _fuelRecordsViewModel;

        public FuelRecordsDataModel() { }

        public bool EditingRecord
        {
            get { return _editingRecord; }
            set
            {
                _editingRecord = value;
                OnPropertyChanged(nameof(EditingRecord));
            }
        }
        
        public float? AmountRefueled 
        { 
            get {  return _amountRefueled; } 
            set 
            { 
                _amountRefueled = value;
                OnPropertyChanged(nameof(AmountRefueled));
            }
        }

        public float? DistanceTraveled
        {
            get { return _distanceTraveled; }
            set
            {
                _distanceTraveled = value;
                OnPropertyChanged(nameof(DistanceTraveled));
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

        public string Date
        {
            // convert DateTime to string
            get
            {
                string day = _date.Day.ToString();
                string month = _date.Month.ToString();
                string year = _date.Year.ToString();
                string finalDateFormat = day + "." + month + "." + year;
                return finalDateFormat;
            }
            // convert string to DateTime
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

        public string AdditionNote
        {
            get { return _additionNote; }
            set
            {
                _additionNote = value;
                OnPropertyChanged(nameof(AdditionNote));
            }
        }

        public FuelRecordsViewModel FuelRecordsViewModel
        {
            get { return _fuelRecordsViewModel; }
            set 
            { 
                _fuelRecordsViewModel = value; 
                OnPropertyChanged(nameof(FuelRecordsViewModel));
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
            // check if we can create new record with everything we need
            if (_distanceTraveled != 0 && _amountRefueled != 0 && _distanceTraveled != null & _amountRefueled != null) 
            {
                return true;
            }
            return false;
        }

        private void EndDialogWindow_OK(object obj)
        {
            if (!EditingRecord)
            {
                // adding new record
                FuelRecordsViewModel.FuelRefuelList.Add(this);
            }
        }
    }
}
