using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SemesterWork___car_data_database.Support;

namespace SemesterWork___car_data_database.Models
{
    public class FuelRecordsViewModel : ViewModelBase
    {
        // FuelConsumptionRecords window
        private FuelRecordsDataModel _selectedFuelConsumptionData;
        private MainWindowViewModel _mainWindowViewModel;
        private RelayCommand _removeCommand;
        private RelayCommand _okCommand;
        private int? _selectedDataIndex;

        public ObservableCollection<FuelRecordsDataModel> FuelRefuelList { get; set; } = new ObservableCollection<FuelRecordsDataModel>();

        public FuelRecordsViewModel() { }

        public FuelRecordsDataModel SelectedFuelConsumptionData
        {
            get { return _selectedFuelConsumptionData; }
            set
            {
                _selectedFuelConsumptionData = value;
                OnPropertyChanged(nameof(SelectedFuelConsumptionData));
            }
        }

        public int? SelectedDataIndex
        {
            get
            {
                return _selectedDataIndex;
            }
            set
            {
                _selectedDataIndex = value;
                OnPropertyChanged(nameof(SelectedDataIndex));
            }
        }

        public MainWindowViewModel MainWindowViewModel 
        { 
            get { return _mainWindowViewModel; }
            set
            {
                _mainWindowViewModel = value;
                OnPropertyChanged(nameof(MainWindowViewModel));
            }
        }

        public RelayCommand RemoveCommand
        {
            get
            {
                return _removeCommand ?? (_removeCommand = new RelayCommand(RemoveFuelConsRecord, RemoveCommandCanExecute));
            }
        }

        private bool RemoveCommandCanExecute(object obj)
        {
            // check if we selected something to delete
            if(SelectedFuelConsumptionData == null || !(FuelRefuelList.Count > 0))
            {
                return false;
            }
            return true;
        }

        private void RemoveFuelConsRecord(object obj)
        {
            FuelRefuelList.Remove(SelectedFuelConsumptionData);
            if(FuelRefuelList.Count > 0 )
            {
                SelectedFuelConsumptionData = FuelRefuelList.First();
            }
        }

        public RelayCommand OkCommand
        {
            get
            {
                return _okCommand ?? (_okCommand = new RelayCommand(AddAverageConsumption, OkCommandCanExecute));
            }
        }

        private bool OkCommandCanExecute(object obj)
        {
            // check if we have something to calculate fuel consumption with
            if(FuelRefuelList.Count > 0)
            {
                return true;
            }
            return false;
        }

        private void AddAverageConsumption(object obj)
        {
            MainWindowViewModel.AverageConsumption = CalculateAverageConsumption();
        }

        private float? CalculateAverageConsumption()
        {
            // calculate avg. fuel consumption and save list to main window
            MainWindowViewModel.FuelRefuelList.Clear();
            float? consumptionSum = 0;
            float? distanceTraveledSum = 0;
            for( int i = 0; i < FuelRefuelList.Count; i++ )
            {
                consumptionSum += FuelRefuelList[i].AmountRefueled;
                distanceTraveledSum += FuelRefuelList[i].DistanceTraveled;
                MainWindowViewModel.FuelRefuelList.Add(FuelRefuelList[i]);
            }
            float? averageConsumption = (consumptionSum / distanceTraveledSum) * 100;

            return averageConsumption;
        }
    }
}
