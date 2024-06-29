using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace SemesterWork___car_data_database.Models
{
    public class MainWindowViewModel : ViewModelBase
    {
        private string _SPZ = string.Empty;
        private string _manufacturer = string.Empty;    
        private string _carModel = string.Empty;
        private float? _averageConsumtion = 0;
        private float? _carMileage = 0;
        private int? _modelYear = null;
        private int? _selectedListIndex = null;
        private ServiceRecordsDataModel _selectedRemindrsData;

        public ObservableCollection<ServiceRecordsDataModel> RemindersList { get; set; } = new ObservableCollection<ServiceRecordsDataModel>();
        public ObservableCollection<FuelRecordsDataModel> FuelRefuelList { get; set; } = new ObservableCollection<FuelRecordsDataModel>();
        public ObservableCollection<ServiceRecordsDataModel> ServiceList { get; set; } = new ObservableCollection<ServiceRecordsDataModel>();

        public MainWindowViewModel() { }

        public ServiceRecordsDataModel SelectedRemindersData
        {
            get { return _selectedRemindrsData; }
            set 
            { 
                _selectedRemindrsData = value;
                OnPropertyChanged(nameof(SelectedRemindersData));
            }
        }

        public string SPZ
        {
            get { return _SPZ; }
            set
            {
                _SPZ = value;
                OnPropertyChanged(nameof(SPZ));
            }
        }

        public string Manufacturer
        {
            get { return _manufacturer; }
            set
            {
                _manufacturer = value;
                OnPropertyChanged(nameof(Manufacturer));
            }
        }

        public string CarModel
        {
            get { return _carModel; }
            set
            {
                _carModel = value;
                OnPropertyChanged(nameof(CarModel));
            }
        }

        public int? SelectedListIndex
        {
            get { return _selectedListIndex; }
            set
            {
                _selectedListIndex = value;
                OnPropertyChanged(nameof(SelectedListIndex));
            }
        }

        public float? AverageConsumption
        {
            get { return _averageConsumtion; }
            set
            {
                _averageConsumtion = value;
                OnPropertyChanged(nameof(AverageConsumption));
            }
        }

        public float? CarMileage
        {
            get { return _carMileage; }
            set
            {
                _carMileage = value;
                OnPropertyChanged(nameof(CarMileage));
            }
        }

        public int? ModelYear
        {
            get { return _modelYear; }
            set
            {
                _modelYear = value;
                OnPropertyChanged(nameof(ModelYear));
            }
        }
    }
}
