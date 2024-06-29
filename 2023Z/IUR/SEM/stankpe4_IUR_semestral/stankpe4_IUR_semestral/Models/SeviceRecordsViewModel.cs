using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SemesterWork___car_data_database.Support;

namespace SemesterWork___car_data_database.Models
{
    public class ServiceRecordsViewModel : ViewModelBase
    {
        private MainWindowViewModel _mainWindowViewModel;
        private RelayCommand _addCommand;
        private RelayCommand _removeCommand;
        private RelayCommand _okCommand;
        private ServiceRecordsDataModel _selectedServiceData;
        private int? _selectedServiceDataIndex;

        public ObservableCollection<ServiceRecordsDataModel> ServiceList { get; set; } = new ObservableCollection<ServiceRecordsDataModel>();

        public ServiceRecordsViewModel() { }

        public ServiceRecordsDataModel SelectedServiceData
        {
            get { return _selectedServiceData; } 
            set
            {
                _selectedServiceData = value;
                OnPropertyChanged(nameof(SelectedServiceData));
            }
        }

        public int? SelectedServiceDataIndex
        {
            get { return _selectedServiceDataIndex; }
            set
            {
                _selectedServiceDataIndex = value;
                OnPropertyChanged(nameof(SelectedServiceDataIndex));
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
                return _removeCommand ?? (_removeCommand = new RelayCommand(RemoveServiceRecord, RemoveCommandCanExecute));
            }
        }

        private bool RemoveCommandCanExecute(object obj)
        {
            // check if we can remove data
            if(SelectedServiceData == null)
            {
                return false;
            }
            return true;
        }

        private void RemoveServiceRecord(object obj)
        {
            // deleted selected service record
            ServiceList.Remove(SelectedServiceData);
            if(ServiceList.Count > 0)
            {
                SelectedServiceData = ServiceList.First();
            }
        }

        public RelayCommand OkCommand
        {
            get
            {
                return _okCommand ?? (_okCommand = new RelayCommand(AddServiceData, OkCommandCanExecute));
            }
        }

        private bool OkCommandCanExecute(object obj)
        {
            // check if we can try adding reminders to main window
            if (ServiceList.Count > 0)
            {
                return true;
            }
            return false;
        }

        private void AddServiceData(object obj)
        {
            MainWindowViewModel.RemindersList.Clear();
            MainWindowViewModel.ServiceList.Clear();
            DateTime currentDate = DateTime.Now.Date;
            for(int i = 0; i < ServiceList.Count; i++)
            {
                ServiceRecordsDataModel serviceData = ServiceList[i];
                // save the list in main window
                MainWindowViewModel.ServiceList.Add(ServiceList[i]);
                // check if we have something to be added into reminders
                // if < 14 bigger prio - TO be added
                if (!serviceData.ServiceFinished && serviceData.GreenCard)
                {
                    
                    if ((currentDate - DateTime.Parse(serviceData.Date).Date).TotalDays > 330)
                    {
                        MainWindowViewModel.RemindersList.Add(serviceData);
                    }
                }
                // if < 30 bigger prio - TO be added
                else if (!serviceData.ServiceFinished && serviceData.STK)
                {
                    if (currentDate.Year - MainWindowViewModel.ModelYear <= 4)
                    {
                        if ((currentDate - DateTime.Parse(serviceData.Date).Date).TotalDays > 1395)
                        {
                            MainWindowViewModel.RemindersList.Add(serviceData);
                        }
                    }
                    else
                    {
                        if((currentDate - DateTime.Parse(serviceData.Date).Date).TotalDays > 665)
                        {
                            MainWindowViewModel.RemindersList.Add(serviceData);
                        }
                    }
                }
                else if(!serviceData.ServiceFinished && !serviceData.STK && !serviceData.GreenCard)
                {
                    MainWindowViewModel.RemindersList.Add(serviceData);
                }

            }
        }

        
    }
}
