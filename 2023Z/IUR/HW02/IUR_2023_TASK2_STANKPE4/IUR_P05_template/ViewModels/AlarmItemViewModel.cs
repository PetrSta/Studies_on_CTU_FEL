using Accessibility;
using IUR_P05_solved.Support;
using IUR_P05_solved.ViewModels.Types;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace IUR_P05_solved.ViewModels
{
    public class AlarmItemViewModel : ViewModelBase
    {
        private string _alarmName;
        private AlarmType _type;
        private int _sliderValue;
        private string _alarmMessage;
        private BitmapImage _selectedImage;

        public ObservableCollection<BitmapImage> AlarmIcons { get; set; } = new ObservableCollection<BitmapImage>();

        public AlarmItemViewModel()
        {
            AlarmIcons.Add(new BitmapImage(new Uri("pack://application:,,,/Images/cold1.png")));
            AlarmIcons.Add(new BitmapImage(new Uri("pack://application:,,,/Images/cold2.png")));
            AlarmIcons.Add(new BitmapImage(new Uri("pack://application:,,,/Images/cold3.png")));
            AlarmIcons.Add(new BitmapImage(new Uri("pack://application:,,,/Images/hot1.png")));
            AlarmIcons.Add(new BitmapImage(new Uri("pack://application:,,,/Images/hot2.png")));
            AlarmIcons.Add(new BitmapImage(new Uri("pack://application:,,,/Images/hot3.png")));
        }

        public string AlarmName
        {
            set 
            {
                _alarmName = value;
                OnPropertyChanged(nameof(AlarmName));
            }
            get { return _alarmName; }
        }

        public AlarmType Type
        {
            get { return _type; }
            set
            {
                _type = value;
                OnPropertyChanged(nameof(Type));
            }
        }

        public int SliderValue
        {
            get { return _sliderValue; }
            set
            {
                _sliderValue = value;
                OnPropertyChanged(nameof(SliderValue));
            }
        }

        public string AlarmMessage
        {
            get { return _alarmMessage; }
            set
            {
                _alarmMessage = value;
                OnPropertyChanged(nameof(AlarmMessage));
            }
        }

        public BitmapImage SelectedImage
        {
            get { return _selectedImage; }
            set
            {
                _selectedImage = value;
                OnPropertyChanged(nameof(SelectedImage));
            }
        }
    }
}
