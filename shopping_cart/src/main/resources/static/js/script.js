$(function () {
    var $userRegister = $("#userRegister");
    $userRegister.validate(
        {
            rules: {
                fullName: {
                    required: true,
                    lettersonly: true
                },
                email: {
                    required: true,
                    email: true,
                    space: true
                },
                phoneNumber: {
                    required: true,
                    numericOnly: true,
                    minlength: 10,
                    maxlength: 12,
                    space: true
                },
                address: {
                    required: true,

                },
                city: {
                    required: true,

                },
                state: {
                    required: true,

                },
                pinCode: {
                    required: true,
                    numericOnly: true,
                    space: true

                },
                password: {
                    required: true,
                    space: true

                },
                confirmpw: {
                    required: true,
                    equalTo: '#password'

                },
                img: {
                    required: true,
                }


            },
            messages: {
                fullName: {
                    required: 'this field is required',
                    lettersonly: 'invalid name'
                },
                email: {
                    required: 'this field is required',
                    email: 'email is invalid @',
                    space: 'space not allowed'
                },
                phoneNumber: {
                    required: 'this field is required',
                    numericOnly: 'only number',
                    minlength: 'at least 10 characters',
                    maxlength: 'max 12 character',
                    space: 'space not allowed'
                },
                address: {
                    required: 'this field is required',

                },
                city: {
                    required: 'this field is required',

                },
                state: {
                    required: 'this field is required',

                },
                pinCode: {
                    required: 'this field is required',
                    numericOnly: 'only number',
                    space: 'space is not allowed'

                },
                password: {
                    required: 'this field is required',
                    space: 'space is not allowed'

                },
                confirmpw: {
                    required: 'this field is required',
                    equalTo: 'password must same as password has entered'

                },
                img: {
                    required: 'this field is required',
                }
            }
        }
    );

    var $userOrder = $("#userOrder");
    $userOrder.validate(
        {
            rules: {
                firstName: {
                    required: true,
                    lettersonly: true
                },
                lastName: {
                    required: true,
                    lettersonly: true
                },
                email: {
                    required: true,
                    email: true,
                    space: true
                },
                mobileNo: {
                    required: true,
                    numericOnly: true,
                    space: true
                },
                address: {
                    required: true,

                },
                city: {
                    required: true,

                },
                state: {
                    required: true,

                },
                pinCode: {
                    required: true,
                    numericOnly: true,
                    space: true
                },
                paymentType: {
                    required: true,

                }

            },
            messages: {
                firstName: {
                    required: 'this field is required',
                    lettersonly: 'only letters'
                },
                lastName: {
                    required:'this field is required',
                    lettersonly:  'only letters'
                },
                email: {
                    required: 'this field is required',
                    email: 'must contain @ character',
                    space: 'characters must no space'
                },
                mobileNo: {
                    required: 'this field is required',
                    numericOnly: 'only numbers',
                    space: 'characters must no space'
                },
                address: {
                    required: 'this field is required',

                },
                city: {
                    required: 'this field is required',

                },
                state: {
                    required: 'this field is required',

                },
                pinCode: {
                    required: 'this field is required',
                    numericOnly: 'only numbers',
                    space: 'characters must no space'
                },
                paymentType: {
                    required: 'this field is required',

                }
            }
        }
    );


});
jQuery.validator.addMethod('lettersonly', function(value, element){
    return /^[^-\s][a-zA-Z-_\s]+$/.test(value);
}, 'data isnot valided');
jQuery.validator.addMethod('space', function(value, element){
    return /^[^\s]+$/.test(value);
}, 'space is not allowed');
jQuery.validator.addMethod('numericOnly', function(value, element){
    return /^[0-9]+$/.test(value);
}, 'only numbers');