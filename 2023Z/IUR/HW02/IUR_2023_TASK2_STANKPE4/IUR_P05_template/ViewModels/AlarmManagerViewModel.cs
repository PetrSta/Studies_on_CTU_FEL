using IUR_P05_solved.Support;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace IUR_P05_solved.ViewModels
{
    class AlarmManagerViewModel : ViewModelBase
    {
        private AlarmItemViewModel _selectedAlarmDetail;
        private RelayCommand _addCommand;
        private RelayCommand _deleteCommand;
        private int _selectedAlarmIndex;

        public ObservableCollection<AlarmItemViewModel> AlarmList { get; set; } = new ObservableCollection<AlarmItemViewModel>();

        public AlarmManagerViewModel()
        {
            AlarmList.Add(new AlarmItemViewModel() { AlarmName = "Alarm 1" });
            AlarmList.Add(new AlarmItemViewModel() { AlarmName = "Alarm 2" });
            AlarmList.Add(new AlarmItemViewModel() { AlarmName = "Alarm 3" });
            _selectedAlarmDetail = AlarmList.First();
        }
        
        public AlarmItemViewModel SelectedAlarmDetail
        {
            get { return _selectedAlarmDetail; }
            set
            {
                _selectedAlarmDetail = value;
                OnPropertyChanged(nameof(SelectedAlarmDetail));
            }
        }

        public RelayCommand AddCommand
        {
            get
            {
                return _addCommand ?? (_addCommand = new RelayCommand(AddAlarmItem, AddCommandCanExecute));
            }
        }

        public int SelectedAlarmIndex
        {
            get
            {
                return _selectedAlarmIndex;
            }
            set
            {
                _selectedAlarmIndex = value;
                OnPropertyChanged(nameof(SelectedAlarmIndex));
            }
        }

        private bool AddCommandCanExecute(object obj)
        {
            if (AlarmList.Count >= 10)
            {
                return false;
            }
            return true;
        }

        private void AddAlarmItem(object obj)
        {
            AlarmItemViewModel newAlarm = new AlarmItemViewModel() { AlarmName = "NewAlarm" };
            AlarmList.Add(newAlarm);
            SelectedAlarmDetail = newAlarm;
        }

        public RelayCommand DeleteCommand
        {
            get
            {
                return _deleteCommand ?? (_deleteCommand = new RelayCommand(RemoveAlarmItem, DeleteCommandCanExecute));
            }
        }

        private bool DeleteCommandCanExecute(object obj)
        {
            if (SelectedAlarmDetail == null)
            {
                return false;
            }
            return true;
        }

        private void RemoveAlarmItem(object obj)
        {
            AlarmList.Remove(SelectedAlarmDetail);
            if (AlarmList.Count > 0)
            {
                SelectedAlarmDetail = AlarmList.First();
            }
        }
    }
}
