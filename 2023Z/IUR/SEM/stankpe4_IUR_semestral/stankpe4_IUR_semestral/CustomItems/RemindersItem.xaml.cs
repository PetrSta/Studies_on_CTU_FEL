using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace SemesterWork___car_data_database.CustomItems
{
    /// <summary>
    /// Interakční logika pro RemindersItem.xaml
    /// </summary>
    public partial class RemindersItem : UserControl
    {
        public static readonly DependencyProperty DateProperty = DependencyProperty.Register("Date", typeof(string), typeof(RemindersItem));
        public static readonly DependencyProperty ReminderTagProperty = DependencyProperty.Register("ReminderTag", typeof(string), typeof(RemindersItem));

        public string Date
        {
            get => (string)GetValue(DateProperty);
            set => SetValue(DateProperty, value);
        }

        public string ReminderTag
        {
            get => (string)GetValue(ReminderTagProperty);
            set => SetValue(ReminderTagProperty, value);
        }

        public RemindersItem()
        {
            InitializeComponent();
        }
    }
}
