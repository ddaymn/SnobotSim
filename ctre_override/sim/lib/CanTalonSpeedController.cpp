/*
 * CanTalonSpeedController.cpp
 *
 *  Created on: May 26, 2017
 *      Author: preiniger
 */

#include "CanTalonSpeedController.h"
#include <iostream>

CanTalonSpeedController::CanTalonSpeedController(int aHandle) :
    SpeedControllerWrapper(aHandle),
    mLastSetValue(0)
{
    SetName("CAN " + GetName());

    mControlMode = ControlMode_Unknown;
}

CanTalonSpeedController::~CanTalonSpeedController()
{

}

void CanTalonSpeedController::SmartSet(double aValue)
{
    mLastSetValue = aValue;
}

double CanTalonSpeedController::GetLastSetValue() const
{
    return mLastSetValue;
}

void CanTalonSpeedController::SetControlMode(ControlMode aControlMode)
{
    mControlMode = aControlMode;

    switch(mControlMode)
    {
    case ControlMode_Disabled:
    {
        break;
    }
    case ControlMode_ThrottleMode:
    {
        SetVoltagePercentage(mLastSetValue);
        break;
    }
    case ControlMode_Follower:
    {
        std::cerr << "This shouldn't be called directly" << std::endl;
        break;
    }
    default:
        std::cerr << "Unknown control mode" << std::endl;
    }
}

void CanTalonSpeedController::AddFollower(const std::shared_ptr<CanTalonSpeedController>& aFollower)
{
    mFollowers.push_back(aFollower);
}

void CanTalonSpeedController::Update(double aWaitTime)
{
    SpeedControllerWrapper::Update(aWaitTime);

    for(unsigned int i = 0; i < mFollowers.size(); ++i)
    {
        mFollowers[i]->SetVoltagePercentage(GetVoltagePercentage());
    }
}



