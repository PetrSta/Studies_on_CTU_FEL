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
    /// Interakční logika pro ServiceItem.xaml
    /// </summary>
    public partial class ServiceItem : UserControl
    {
        public static readonly DependencyProperty DateProperty = DependencyProperty.Register("Date", typeof(string), typeof(ServiceItem));
        public static readonly DependencyProperty RecordTagProperty = DependencyProperty.Register("RecordTag", typeof(string), typeof (ServiceItem));

        public string Date
        {
            get => (string)GetValue(DateProperty);
            set => SetValue(DateProperty, value);
        }

        public string RecordTag
        {
            get => (string)GetValue(RecordTagProperty); 
            set => SetValue(RecordTagProperty, value);
        }

        public static readonly RoutedEvent DoubleClickEvent = EventManager.RegisterRoutedEvent("DoubleClick", RoutingStrategy.Bubble, typeof(RoutedEventHandler), typeof(ServiceItem));

        public event RoutedEventHandler DoubleClickHandler
        {
            add { AddHandler(DoubleClickEvent, value); }
            remove { RemoveHandler(DoubleClickEvent, value); }
        }

        private void DoubleCLick(object sender, MouseButtonEventArgs e)
        {
            if (e.ClickCount == 2)
            {
                RaiseEvent(new RoutedEventArgs(DoubleClickEvent));
            }
        }

        public ServiceItem()
        {
            InitializeComponent();
        }
    }
}
