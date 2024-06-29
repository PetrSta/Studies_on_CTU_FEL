using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace SemesterWork___car_data_database.Support
{
    internal class UnitsFormatConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            // set float decimal places to 1 and add desired unit at the end of string
            string stringValue = ((float)value).ToString("0.0");
            string units = (string)parameter;
            return stringValue + units;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            // get the float value of the string, in case of wrong result set the value to default -> 0
            float floatValue;
            string stringValue = (string)value;
            try
            {
                if (stringValue.Contains(","))
                {
                    floatValue = float.Parse(((string)value));
                }
                else
                {
                    floatValue = float.Parse(((string)value), CultureInfo.InvariantCulture.NumberFormat);
                }
                if(floatValue < 0)
                {
                    floatValue = 0;
                }
            }
            catch
            {
                floatValue = 0;
            }
            return floatValue;
        }
    }
}
